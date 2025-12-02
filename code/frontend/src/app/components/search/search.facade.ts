import { Injectable, inject, signal, computed } from '@angular/core';
import { of, defer, iif, forkJoin, from, VirtualTimeScheduler } from 'rxjs';
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
  Search,
  Utility,
} from '../../services/models';
import { AdCategory } from '../../enums/ad-category.enum';
import { CadastralDataControllerService } from '../../services/services';

export type SearchCard = RealEstateResponse & {
  title?: string;
  address?: string;
  city?: string;
};

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

  private _categoryCache = new Map<number, AdCategory>();
  private _cachedCategory = signal<AdCategory | null>(null);

  private _detailCache = new Map<number, Detail>();
  private _cachedDetail = signal<Detail | null>(null);

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

  loading = signal(false);
  error = signal<string | null>(null);
  geographicalPositionId = signal<number | null>(null);
  utilityId = signal<number | null>(null);
  detailId = signal<number | null>(null);
  cadastralFilterId = signal<number | null>(null);
  savedSearches = signal<Search[]>([]);

  searchCards = signal<SearchCard[]>([]);

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

  cacheFilters(
    geographicalPositionRequest: GeographicalPositionRequest,
    utilityRequest: UtilityRequest,
    cadastralFilterRequest: CadastralFilterRequest,
    category?: AdCategory,
  ) {
    this._cachedGeographicalPosition.set(geographicalPositionRequest);
    this._cachedUtility.set(utilityRequest);
    //this._cachedCadastralFilter.set(cadastralFilterRequest);
    if (category) this._cachedCategory.set(category);
  }

  replaySearch(search: Search) {
    this.resetContext();

    this._cachedGeographicalPosition.set(search.detail?.geographicalPosition!);
    this._cachedUtility.set(search.detail?.utility!);
    this._cachedCadastralFilter.set(search.cadastralFilter!);
    this._cachedCategory.set(search.category as AdCategory);

    return this.runFullSearch({
      category: search.category as AdCategory,
      geographicalPosition: search.detail
        ?.geographicalPosition as GeographicalPositionRequest,
      utility: search.detail?.utility as UtilityRequest,
      cadastralFilter: search.cadastralFilter as CadastralFilterRequest,
    });
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

  getSearchDetailAndCadastralFilter(
    detailId: number,
    cadastralFilterId: number,
  ) {
    return forkJoin({
      detail: this.detailService.getDetailById({
        detailid: detailId,
      }),
      cadastralFilter: this.cadastralFilterService.getCadastralFilterById({
        cadastralfilterid: cadastralFilterId,
      }),
    });
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
      switchMap((realEstateList) => {
        const requests = realEstateList.map((realEstate) => {
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
            realEstate: of(realEstate),
            cadastralData: cadastralData,
            details: details,
          }).pipe(
            map((result) => {
              return {
                ...result.realEstate,
                cadastralData: result.cadastralData,
                geographicalPosition: result.details.geographicalPosition,
                utility: result.details.utility,
              };
            }),
          );
        });

        return forkJoin(requests);
      }),
      map((realEstateObservables) => {
        return realEstateObservables.map((realEstate) => {
          return {
            category: realEstate.category,
            createdDate: realEstate.createdDate,
            description: realEstate.description,
            estateAgentEmail: realEstate.estateAgentEmail,
            images: realEstate.images,
            title: '',
            address: realEstate.geographicalPosition.address,
            city: realEstate.geographicalPosition.city,
          } as SearchCard;
        });
      }),
      tap((searchCards) => {
        this.searchCards.set(searchCards);
      }),
    );
  }

  loadUserSearches() {}
}
