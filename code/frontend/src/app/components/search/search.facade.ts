import { Injectable, inject, signal, computed } from '@angular/core';
import { of, defer, iif, forkJoin, from } from 'rxjs';
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
import { DetailRequest } from '../../services/models/detail-request';
import { CadastralFilterRequest } from '../../services/models/cadastral-filter-request';
import { environment } from '../../../environments/environment.development';
import {
  CadastralFilter,
  CadastralFilterResponse,
  Detail,
  DetailResponse,
  GeographicalPosition,
  Search,
  Utility,
} from '../../services/models';
import { GetDetailGeographicalPosition$Params } from '../../services/fn/geographical-position-controller/get-detail-geographical-position';
import { AdCategory } from '../../enums/ad-category.enum';
import { ReactiveFormsModule } from '@angular/forms';

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
  private authService = inject(AuthService);

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
  private _lastForm = signal<{
    category: AdCategory;
    page: number; // 1-based
    size: number; // >0
    userEmail: string;
  } | null>(null);

  loading = signal(false);
  error = signal<string | null>(null);
  geographicalPositionId = signal<number | null>(null);
  utilityId = signal<number | null>(null);
  detailId = signal<number | null>(null);
  cadastralFilterId = signal<number | null>(null);
  savedSearches = signal<Search[]>([]);

  lastForm = this._lastForm.asReadonly();
  searchCards = signal<SearchCard[]>([]);
  hasNext = computed(
    () => this.searchCards().length >= (this._lastForm()?.size ?? 0),
  );
  hasPrev = computed(() => (this._lastForm()?.page ?? 1) > 1);

  constructor() {
    this.authService
      .getUserInfo()
      .pipe(
        tap((userInfo) => {
          const email = userInfo?.email?.trim();
          this._userEmail.set(email);
          this._authenticated.set(this.authService.isAuthenticated());
          this.loadRecentFor(email);
        }),
        catchError(() => {
          this._userEmail.set(null);
          this._authenticated.set(this.authService.isAuthenticated());
          this.loadRecentFor(null);
          return of(null);
        }),
      )
      .subscribe();
  }

  cacheFilters(
    geographicalPosition: GeographicalPosition,
    utility: Utility,
    cadastralFilter: CadastralFilter,
  ) {
    this._cachedGeographicalPosition.set(geographicalPosition);
    this._cachedUtility.set(utility);
    this._cachedCadastralFilter.set(cadastralFilter);
  }

  private _getCachedGeographicalPosition() {
    return this._cachedGeographicalPosition();
  }

  private _getCachedUtility() {
    return this._cachedUtility();
  }

  private _getCachedCadastralFilter() {
    return this._cachedCadastralFilter();
  }

  private _persistSavedSearches(search: Search, max = 10) {
    const email = this._currentEmail();
    const key = this._recentKey(email);

    const current = [...this.savedSearches()];
    const without = current.filter(
      (searchFilter) => searchFilter.id !== search.id,
    );
    const next = [search, ...without].slice(0, max);

    this.savedSearches.set(next);
    try {
      localStorage.setItem(key, JSON.stringify(next));
    } catch {}
  }

  private _getDetail(detailId: number) {
    const cachedDetail = this._detailCache.get(detailId);
    if (cachedDetail) return of(cachedDetail);

    return this.detailService.getDetailById({ detailid: detailId }).pipe(
      map((detail: any) => {
        const output = {
          geographicalPositionId:
            detail?.geographicalPositionId ?? detail?.geographicalPosition?.id,
        };
        this._detailCache.set(detailId, output);
        return output;
      }),
    );
  }

  private _getGeographicalPosition(geographicalPositionId: number) {
    const _cachedGeographicalPosition = this._geographicalPositionCache.get(
      geographicalPositionId,
    );
    if (_cachedGeographicalPosition) return of(_cachedGeographicalPosition);

    return this.geographicalPositionService
      .getGeographicalPositionById({
        geographicalpositionid: geographicalPositionId,
      })
      .pipe(
        map((geographicalPosition: any) => {
          const output = {
            latitude: Number(geographicalPosition?.latitude),
            longitude: Number(geographicalPosition?.longitude),
            address: geographicalPosition?.address ?? '',
            city: geographicalPosition?.city ?? '',
          };
          this._geographicalPositionCache.set(geographicalPositionId, output);
          return output;
        }),
      );
  }

  replaySearch(search: Search) {
    this.resetContext();

    this._cachedGeographicalPosition.set(search.detail?.geographicalPosition!);
    this._cachedUtility.set(search.detail?.utility!);
    this._cachedCadastralFilter.set(search.cadastralFilter!);

    return this.runFullSearch({
      category: search.category,
      geographicalPosition: search.detail?.geographicalPosition,
      utility: search.detail?.utility,
      cadastralFilter: search.cadastralFilter,
    }).subscribe();
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
        this.error.set(this._msg(error));
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
          this.error.set(this._msg(error));
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
    this._lastForm.set(null);
  }

  createSearchCard(
    category: AdCategory,
    realEstateImages: string[],
    geographicalPosition: GeographicalPosition,
  ) {
    const latitude = geographicalPosition.latitude;
    const longitude = geographicalPosition.longitude;

    const images = [];
    for (const realEstateImage of realEstateImages) {
      images.push(`${environment.apiBaseUrl}${realEstateImage}`);
    }

    return {
      images: images,
      category,
      title: geographicalPosition.address,
      address: geographicalPosition.address,
      city: geographicalPosition.city,
    } as SearchCardGeo;
  }

  createSearchDetailAndCadastralFilter(
    detailId: number,
    cadastralFilterId: number,
  ) {
    return forkJoin({
      searchDetail: this.detailService.getDetailById({
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
      searchGeographicalPosition:
        this.geographicalPositionService.getGeographicalPositionById({
          geographicalpositionid: detail.geographicalPositionId!,
        }),
      searchUtility: this.utilityService.getUtilityById({
        utilityid: detail.utilityId!,
      }),
      cadastralFilter: of(cadastralFilter),
    });
  }

  search(params: { category: AdCategory }) {
    this.error.set(null);

    const body: SearchRequest = {
      category: params.category,
      detailId: this.detailId()!,
      cadastralFilterId: this.cadastralFilterId()!,
    };

    this.loading.set(true);
    this._lastForm.set({ ...params, userEmail: resolvedEmail });

    this.createSearchDetailAndCadastralFilter(
      this.detailId()!,
      this.cadastralFilterId()!,
    ).pipe(
      switchMap((fetched) =>
        this.getDetailUtilityAndGeographicalPosition(
          fetched.searchDetail,
          fetched.cadastralFilter,
        ),
      ),
      switchMap((fetched) => {
        return this.searchService.createSearch({ body }).pipe(
          map((realEstateList) => {
            return realEstateList.map((realEstate) =>
              this.createSearchCard(
                realEstate.category as AdCategory,
                realEstate.images!,
                fetched.geographicalPosition,
              ),
            );
          }),
        );
      }),
      switchMap((searchCards) => {
        const missing = searchCards.fil;
      }),
    );

    return guard$.pipe(
      switchMap(() => this.searchService.createSearch({ body })),

      switchMap((searchCards) => {
        const detailIds = Array.from(
          new Set(
            missing
              .map((searchCard) => Number((searchCard as any).detailId))
              .filter(Number.isFinite),
          ),
        ) as number[];
        if (detailIds.length === 0) return of(searchCards);

        return forkJoin(
          detailIds.map((detailId) => this._getDetail(detailId)),
        ).pipe(
          switchMap((details) => {
            const mapDetailToGeographicalPosition = new Map<number, number>();

            details.forEach((detail, idx) => {
              const detailId = detailIds[idx];
              const geographicalPositionId = Number(
                detail?.geographicalPositionId,
              );
              if (Number.isFinite(geographicalPositionId))
                mapDetailToGeographicalPosition.set(
                  detailId,
                  geographicalPositionId,
                );
            });

            const geographicalPositionIds = Array.from(
              new Set(
                Array.from(mapDetailToGeographicalPosition.values()).filter(
                  Number.isFinite,
                ),
              ),
            ) as number[];

            if (geographicalPositionIds.length === 0)
              return of({ searchCards, mapDetailToGeographicalPosition });

            return forkJoin(
              geographicalPositionIds.map((geographicalPositionId) =>
                this._getGeographicalPosition(geographicalPositionId),
              ),
            ).pipe(
              map((geographicalPositions) => {
                const mapGeographicalPosition = new Map<number, any>();
                geographicalPositions.forEach((geographicalPosition, idx) =>
                  mapGeographicalPosition.set(
                    geographicalPositionIds[idx],
                    geographicalPosition,
                  ),
                );
                return {
                  searchCards,
                  mapDetailToGeographicalPosition,
                  mapGeographicalPosition,
                };
              }),
            );
          }),

          map(
            ({
              searchCards,
              mapDetailToGeographicalPosition,
              mapGeographicalPosition,
            }: any) =>
              searchCards.map((search: any) => {
                const detailId = Number(search.detailId);
                const geographicalPositionId =
                  mapDetailToGeographicalPosition?.get(detailId);
                const geographicalPosition =
                  geographicalPositionId != null
                    ? mapGeographicalPosition?.get(geographicalPositionId)
                    : null;

                const lat = Number.isFinite(search.lat)
                  ? search.lat
                  : Number(geographicalPosition?.latitude);
                const lon = Number.isFinite(search.lon)
                  ? search.lon
                  : Number(geographicalPosition?.longitude);
                const address =
                  search.address || geographicalPosition?.address || '';
                const city = search.city || geographicalPosition?.city || '';

                return {
                  ...search,
                  lat: Number.isFinite(lat) ? lat : undefined,
                  lon: Number.isFinite(lon) ? lon : undefined,
                  address,
                  city,
                } as SearchCardGeo;
              }),
          ),
        );
      }),

      map((searchCards: SearchCardWithCat[]) =>
        searchCards.filter((searchCard): searchCard is SearchCardWithCat =>
          this._isRequestedCategory(searchCard, requestedCategory),
        ),
      ),

      tap((mapped) => {
        this.searchCards.set(mapped);
        const geographicalPosition = this._cachedGeographicalPosition();
        const utility = this._cachedUtility();
        const cadastralFilter = this._cachedCadastralFilter();
        const last = this._lastForm();
        if (geographicalPosition && utility && cadastralFilter && last) {
          const label = [
            geographicalPosition.city ||
              geographicalPosition.municipality ||
              geographicalPosition.address ||
              'Ricerca',
            (last.category || '').toString(),
            cadastralFilter.priceRange?.minPrice
              ? `≥€${cadastralFilter.priceRange.minPrice}`
              : '',
            cadastralFilter.priceRange?.maxPrice
              ? `≤€${cadastralFilter.priceRange.maxPrice}`
              : '',
          ]
            .filter(Boolean)
            .join(' · ');
          const snapshot: Search = {
            id: `${this._hash({
              geographicalPosition,
              utility,
              cadastralFilter,
              category: last.category,
            })}-${Date.now()}`, // univoco
            when: new Date().toISOString(),
            category: last.category,
            size: last.size,
            page: last.page,
            userEmail: last.userEmail,
            geographicalPosition,
            utility,
            cadastralFilter,
            label,
          };
          this._persistSavedSearches(snapshot);
        }
      }),
      catchError((error) => {
        this.error.set(this._msg(error));
        this.searchCards.set([]);
        throw error;
      }),
      finalize(() => this.loading.set(false)),
    );
  }

  loadUserSearches() {}
}
