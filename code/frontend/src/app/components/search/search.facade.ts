import { Injectable, inject, signal } from '@angular/core';
import { of, forkJoin } from 'rxjs';
import { map, switchMap, tap, catchError, finalize } from 'rxjs/operators';

import { SearchControllerService } from '../../services/services/search-controller.service';
import { GeographicalPositionControllerService } from '../../services/services/geographical-position-controller.service';
import { UtilityControllerService } from '../../services/services/utility-controller.service';
import { DetailControllerService } from '../../services/services/detail-controller.service';
import { CadastralFilterControllerService } from '../../services/services/cadastral-filter-controller.service';

import { AuthService } from '../../manual_services/auth/auth.service';

import { RealEstateResponse } from '../../services/models/real-estate-response';
import { SearchRequest } from '../../services/models/search-request';
import { GeographicalPositionRequest } from '../../services/models/geographical-position-request';
import { UtilityRequest } from '../../services/models/utility-request';
import { CadastralFilterRequest } from '../../services/models/cadastral-filter-request';
import {
  CadastralFilter,
  CadastralFilterResponse,
  Detail,
  DetailResponse,
  GeographicalPosition,
  Utility,
} from '../../services/models';
import { AdCategory } from '../../enums/ad-category.enum';
import { CadastralDataControllerService } from '../../services/services';
import { FullRealEstate } from '../../interfaces/full-real-estate';
import { SearchPaginatorRequest } from '../../interfaces/search-paginator-request';
import { FullSearch } from '../../interfaces/full-search';

@Injectable({ providedIn: 'root' })
export class SearchFacade {
  private searchService = inject(SearchControllerService);
  private geographicalPositionService = inject(
    GeographicalPositionControllerService,
  );
  private utilityService = inject(UtilityControllerService);
  private detailService = inject(DetailControllerService);
  private cadastralFilterService = inject(CadastralFilterControllerService);
  private cadastralDataService = inject(CadastralDataControllerService);
  private authService = inject(AuthService);

  private _cachedCategory = signal<AdCategory | null>(null);
  private _detailCache = new Map<number, Detail>();
  private _utilityCache = new Map<number, Utility>();
  private _cachedUtility = signal<Utility | null>(null);

  private _geographicalPositionCache = new Map<number, GeographicalPosition>();
  private _cachedGeographicalPosition = signal<GeographicalPosition | null>(
    null,
  );

  private _cadastralFilterCache = new Map<number, CadastralFilter>();
  private _cachedCadastralFilter = signal<CadastralFilter | null>(null);

  private _userEmail = signal<string | null>(null);
  private _authenticated = signal(false);

  readonly cachedCategory = this._cachedCategory.asReadonly();
  readonly cachedGeographicalPosition =
    this._cachedGeographicalPosition.asReadonly();
  readonly cachedUtility = this._cachedUtility.asReadonly();
  readonly cachedCadastralFilter = this._cachedCadastralFilter.asReadonly();

  loading = signal(false);
  error = signal<string | null>(null);
  geographicalPositionId = signal<number | null>(null);
  utilityId = signal<number | null>(null);
  detailId = signal<number | null>(null);
  cadastralFilterId = signal<number | null>(null);
  savedSearches = signal<FullSearch[]>([]);

  searchCards = signal<FullRealEstate[]>([]);

  constructor() {
    this.authService
      .getUserInfo()
      .pipe(
        tap((userInfo) => {
          const email = userInfo?.email?.trim();
          this._userEmail.set(email);
          this._authenticated.set(this.authService.isAuthenticated());
        }),
        catchError(() => {
          this._userEmail.set(null);
          this._authenticated.set(this.authService.isAuthenticated());
          return of(null);
        }),
      )
      .subscribe();
  }

  getCachedGeographicalPosition() {
    return this._cachedGeographicalPosition();
  }

  getCachedUtility() {
    return this._cachedUtility();
  }

  getCachedCadastralFilter() {
    return this._cachedCadastralFilter();
  }

  cacheFilters(
    geographicalPositionRequest: GeographicalPositionRequest,
    utilityRequest: UtilityRequest,
    cadastralFilterRequest: CadastralFilterRequest,
    category?: AdCategory,
  ) {
    this._cachedGeographicalPosition.set(geographicalPositionRequest);
    this._cachedUtility.set(utilityRequest);
    this._cachedCadastralFilter.set(cadastralFilterRequest as CadastralFilter);
    if (category) this._cachedCategory.set(category);
  }

  prepareDetail(
    geographicalPositionRequest: GeographicalPositionRequest,
    utilityRequest: UtilityRequest,
  ) {
    this.error.set(null);
    this.loading.set(true);

    return forkJoin({
      geographicalPosition:
        this.geographicalPositionService.createGeographicalPosition({
          body: geographicalPositionRequest,
        }),
      utility: this.utilityService.createUtility({ body: utilityRequest }),
    }).pipe(
      tap((results) => {
        this.geographicalPositionId.set(results.geographicalPosition.id!);
        this.utilityId.set(results.utility.id!);
        this._geographicalPositionCache.set(
          results.geographicalPosition.id!,
          results.geographicalPosition,
        );
        this._utilityCache.set(results.utility.id!, results.utility);
      }),
      switchMap((results) => {
        return this.detailService.createDetail({
          body: {
            geographicalPositionId: results.geographicalPosition.id,
            utilityId: results.utility.id,
          },
        });
      }),
      tap((detailResponse) => {
        this.detailId.set(detailResponse.id!);
        this._detailCache.set(detailResponse.id!, detailResponse);
      }),
      catchError((error) => {
        this.error.set(error);
        throw error;
      }),
      finalize(() => this.loading.set(false)),
    );
  }

  prepareCadastralFilter(cadastralFilterRequest: CadastralFilterRequest) {
    this.error.set(null);
    this.loading.set(true);

    return this.cadastralFilterService
      .createCadastralFilter({
        body: cadastralFilterRequest,
      })
      .pipe(
        tap((cadastralFilterResponse) => {
          this.cadastralFilterId.set(cadastralFilterResponse.id!);
          this._cadastralFilterCache.set(
            cadastralFilterResponse.id!,
            cadastralFilterResponse,
          );
        }),
        catchError((error) => {
          this.error.set(error);
          throw error;
        }),
        finalize(() => this.loading.set(false)),
      );
  }

  runFullSearch(params: {
    category: AdCategory;
    geographicalPosition: GeographicalPositionRequest;
    utility: UtilityRequest;
    cadastralFilter: CadastralFilterRequest;
  }) {
    this.error.set(null);
    this.loading.set(true);

    return forkJoin({
      detail: this.prepareDetail(params.geographicalPosition, params.utility),
      cadastralFilter: this.prepareCadastralFilter(params.cadastralFilter),
    }).pipe(
      switchMap((results) =>
        this.getDetailUtilityAndGeographicalPosition(
          results.detail,
          results.cadastralFilter,
        ),
      ),
      switchMap((results) => {
        return this.search({ category: params.category });
      }),
    );
  }

  resetContext() {
    this.geographicalPositionId.set(null);
    this.utilityId.set(null);
    this.detailId.set(null);
    this.cadastralFilterId.set(null);
  }

  getDetailUtilityAndGeographicalPosition(
    detail: DetailResponse,
    cadastralFilter: CadastralFilterResponse,
  ) {
    return forkJoin({
      geographicalPosition:
        this.geographicalPositionService.getGeographicalPositionById({
          geographicalpositionid: detail.geographicalPositionId!,
        }),
      utility: this.utilityService.getUtilityById({
        utilityid: detail.utilityId!,
      }),
      cadastralFilter: of(cadastralFilter),
    });
  }

  search(params: { category: AdCategory }) {
    this.error.set(null);

    const searchBody: SearchRequest = {
      category: params.category,
      detailId: this.detailId()!,
      cadastralFilterId: this.cadastralFilterId()!,
    };

    this.loading.set(true);

    return this.searchService.createSearch({ body: searchBody }).pipe(
      switchMap((realEstateList) =>
        this.mapRealEstatesToFullRealEstates(realEstateList),
      ),
      tap((searchCards) => {
        this.savedSearches.set(searchCards);
        this.searchCards.set(searchCards);
      }),
      finalize(() => this.loading.set(false)),
    );
  }

  private mapRealEstatesToFullRealEstates(realEstates: RealEstateResponse[]) {
    if (!realEstates || realEstates.length === 0) {
      return of([]);
    }

    const requests = realEstates.map((realEstate) => {
      const cadastralData = this.cadastralDataService.getCadastralDataById({
        cadastraldataid: realEstate.cadastralDataId!,
      });

      const details = this.detailService
        .getDetailById({
          detailid: realEstate.detailId!,
        })
        .pipe(
          switchMap((detail) => {
            return forkJoin({
              geographicalPosition:
                this.geographicalPositionService.getGeographicalPositionById({
                  geographicalpositionid: detail.geographicalPositionId!,
                }),
              utility: this.utilityService.getUtilityById({
                utilityid: detail.utilityId!,
              }),
            });
          }),
        );

      return forkJoin({
        realEstate: of(realEstate),
        cadastralData: cadastralData,
        details: details,
      }).pipe(
        map((result) => {
          return {
            geographicalPosition: result.details.geographicalPosition,
            utility: result.details.utility,
            cadastralData: result.cadastralData,
            category: result.realEstate.category,
            createdDate: result.realEstate.createdDate,
            description: result.realEstate.description,
            id: result.realEstate.id,
            images: result.realEstate.images,
          } as FullRealEstate;
        }),
      );
    });

    return forkJoin(requests);
  }

  replaySearch(search: FullSearch) {
    this.resetContext();
    this.loading.set(true);
    this.error.set(null);

    this._cachedGeographicalPosition.set(search.geographicalPosition);
    this._cachedUtility.set(search.utility);
    this._cachedCadastralFilter.set(search.cadastralFilter!);
    this._cachedCategory.set(search.category as AdCategory);

    return this.searchService.getSearch({ searchid: search.id! }).pipe(
      switchMap((realEstates) =>
        this.mapRealEstatesToFullRealEstates(realEstates),
      ),
      tap((searchCards) => {
        this.searchCards.set(searchCards);
      }),
      catchError((error) => {
        this.error.set(error);
        throw error;
      }),
      finalize(() => this.loading.set(false)),
    );
  }

  fetchUserSearches(request: SearchPaginatorRequest) {
    const params = {
      page: request.page - 1,
      size: request.size,
    };
    return this.searchService.getUserSearches(params).pipe(
      switchMap((response) => {
        const requests = response.content!.map((search) => {
          const cadastralFilter =
            this.cadastralFilterService.getCadastralFilterById({
              cadastralfilterid: search.cadastralFilterId!,
            });

          const details = this.detailService
            .getDetailById({ detailid: search.detailId! })
            .pipe(
              switchMap((detail) => {
                return forkJoin({
                  geographicalPosition:
                    this.geographicalPositionService.getGeographicalPositionById(
                      {
                        geographicalpositionid: detail.geographicalPositionId!,
                      },
                    ),
                  utility: this.utilityService.getUtilityById({
                    utilityid: detail.utilityId!,
                  }),
                });
              }),
            );

          return forkJoin({
            search: of(search),
            cadastralFilter: cadastralFilter,
            details: details,
          }).pipe(
            map((result) => {
              return {
                ...result.search,
                cadastralFilter: result.cadastralFilter,
                geographicalPosition: result.details.geographicalPosition,
                utility: result.details.utility,
              };
            }),
          );
        });

        return forkJoin(requests).pipe(
          map((searchObservables) => {
            return {
              ...response,
              fullSerches: searchObservables,
            };
          }),
        );
      }),
      tap((responseFullSearches) => {
        const newSavedSearches: FullSearch[] =
          responseFullSearches.fullSerches.map((search) => {
            return {
              geographicalPosition: search.geographicalPosition,
              utility: search.utility,
              cadastralFilter: search.cadastralFilter,
              category: search.category as AdCategory,
              createdDate: search.createdDate,
              id: search.id,
            };
          });
        this.savedSearches.set(newSavedSearches);
      }),
    );
  }
}
