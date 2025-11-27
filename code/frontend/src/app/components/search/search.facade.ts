import { Injectable, inject, signal, computed } from '@angular/core';
import { of, defer, iif, forkJoin } from 'rxjs';
import { map, switchMap, tap, catchError, finalize } from 'rxjs/operators';

import { SearchControllerService } from '../../services/services/search-controller.service';
import { GeographicalPositionControllerService } from '../../services/services/geographical-position-controller.service';
import { UtilityControllerService } from '../../services/services/utility-controller.service';
import { DetailControllerService } from '../../services/services/detail-controller.service';
import { CadastralFilterControllerService } from '../../services/services/cadastral-filter-controller.service';

import { AuthService } from '../../manual_services/auth.service';

import { RealEstateResponse } from '../../services/models/real-estate-response';
import { SearchRequest } from '../../services/models/search-request';
import { GeographicalPositionRequest } from '../../services/models/geographical-position-request';
import { UtilityRequest } from '../../services/models/utility-request';
import { DetailRequest } from '../../services/models/detail-request';
import { CadastralFilterRequest } from '../../services/models/cadastral-filter-request';
import { SearchGeographicalPosition } from '../../interfaces/searchGeographicalPosition';

export type Category = 'SALE' | 'RENT';

type SearchCardWithCat = SearchCardGeo & { category?: Category | null };

export type SearchCard = RealEstateResponse & {
  title?: string;
  address?: string;
  city?: string;
};

export type SearchCardGeo = SearchCard & {
  lat?: number;
  lon?: number;
};

export interface RecentSearchSnapshot {
  id: string;
  when: string;
  category: Category;
  size: number;
  page: number;
  userEmail: string;
  geographicalPosition: GeographicalPositionRequest;
  utility: UtilityRequest;
  cadastralFilter: CadastralFilterRequest;
  label: string;
}

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

  loading = signal(false);
  error = signal<string | null>(null);

  geographicalPositionId = signal<number | null>(null);
  utilityId = signal<number | null>(null);
  detailId = signal<number | null>(null);
  cadastralFilterId = signal<number | null>(null);

  private _detailCache = new Map<number, { geographicalPositionId?: number }>();
  private _geographicalPositionCache = new Map<
    number,
    { latitude?: number; longitude?: number; address?: string; city?: string }
  >();

  private _userEmail = signal<string | null>(null);
  private _authenticated = signal(false);

  private _lastForm = signal<{
    category: Category;
    page: number; // 1-based
    size: number; // >0
    userEmail: string;
  } | null>(null);

  lastForm = this._lastForm.asReadonly();

  searchCards = signal<SearchCard[]>([]);

  hasNext = computed(
    () => this.searchCards().length >= (this._lastForm()?.size ?? 0),
  );

  hasPrev = computed(() => (this._lastForm()?.page ?? 1) > 1);

  private _cachedGeographicalPosition =
    signal<GeographicalPositionRequest | null>(null);
  private _cachedUtility = signal<UtilityRequest | null>(null);
  private _cachedCadastralFilter = signal<CadastralFilterRequest | null>(null);

  recent = signal<RecentSearchSnapshot[]>([]);

  private _currentEmail(): string | null {
    const email = this._userEmail();
    return email && email.trim() ? email.trim() : null;
  }

  constructor() {
    this.authService
      .getUserInfo()
      .pipe(
        tap((userInfo) => {
          const email = userInfo?.email?.trim() || null;
          this._userEmail.set(email);
          this._authenticated.set(true);
          this.loadRecentFor(email);
        }),
        catchError(() => {
          this._userEmail.set(null);
          this._authenticated.set(false);
          this.loadRecentFor(null);
          return of(null);
        }),
      )
      .subscribe();
  }

  cacheFilters(
    geographicalPositionRequest: GeographicalPositionRequest,
    utilityRequest: UtilityRequest,
    cadastralFilterRequest: CadastralFilterRequest,
  ) {
    this._cachedGeographicalPosition.set(geographicalPositionRequest ?? null);
    this._cachedUtility.set(utilityRequest ?? null);
    this._cachedCadastralFilter.set(cadastralFilterRequest ?? null);
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

  private _recentKey(email: string | null) {
    return `recent-searches:${email ?? 'anon'}`;
  }

  loadRecentFor(email: string | null) {
    try {
      const raw = localStorage.getItem(this._recentKey(email));
      const arr: RecentSearchSnapshot[] = raw ? JSON.parse(raw) : [];
      const cleaned = (arr ?? [])
        .filter((search) => !!search?.id && !!search?.when)
        .sort((a, b) => b.when.localeCompare(a.when));
      this.recent.set(cleaned);
    } catch {
      this.recent.set([]);
    }
  }

  forgetRecent() {
    const email = this._currentEmail();
    localStorage.removeItem(this._recentKey(email));
    this.recent.set([]);
  }

  private _persistRecent(search: RecentSearchSnapshot, max = 10) {
    const email = this._currentEmail();
    const key = this._recentKey(email);

    const current = [...this.recent()];
    const without = current.filter(
      (searchFilter) => searchFilter.id !== search.id,
    );
    const next = [search, ...without].slice(0, max);

    this.recent.set(next);
    try {
      localStorage.setItem(key, JSON.stringify(next));
    } catch {}
  }

  removeRecent(searchId: string) {
    const email = this._currentEmail();
    const key = this._recentKey(email);
    const next = this.recent().filter((search) => search.id !== searchId);
    this.recent.set(next);
    try {
      localStorage.setItem(key, JSON.stringify(next));
    } catch {}
  }

  replaySearch(search: RecentSearchSnapshot) {
    this.resetContext();

    this._cachedGeographicalPosition.set(search.geographicalPosition);
    this._cachedUtility.set(search.utility);
    this._cachedCadastralFilter.set(search.cadastralFilter);

    const email = this._currentEmail() ?? search.userEmail ?? '';

    return this.runFullSearch({
      category: search.category,
      page: Math.max(1, search.page || 1),
      size: Math.max(1, search.size || 10),
      userEmail: email,
      geographicalPosition: search.geographicalPosition,
      utility: search.utility,
      cadastralFilter: search.cadastralFilter,
    }).subscribe();
  }

  private _hash(object: unknown): string {
    const string = JSON.stringify(object, Object.keys(object as any).sort());
    let h = 0,
      i = 0,
      len = string.length;
    while (i < len) {
      h = (h * 31 + string.charCodeAt(i++)) | 0;
    }
    return `h${(h >>> 0).toString(16)}`;
  }

  prepareDetail(
    geographicalPosition: GeographicalPositionRequest,
    utility: UtilityRequest,
  ) {
    this.error.set(null);
    this.loading.set(true);

    const ensureGeographicalPosition$ = iif(
      () => this.geographicalPositionId() != null,
      of(this.geographicalPositionId()!),
      this.geographicalPositionService
        .createGeographicalPosition({ body: geographicalPosition })
        .pipe(
          map(
            (geographicalPositionResponse) =>
              geographicalPositionResponse?.id ?? null,
          ),
          tap((geographicalPositionId) =>
            this.geographicalPositionId.set(geographicalPositionId),
          ),
        ),
    );

    const ensureUtility$ = iif(
      () => this.utilityId() != null,
      of(this.utilityId()!),
      this.utilityService.createUtility({ body: utility }).pipe(
        map((utilityResponse) => utilityResponse?.id ?? null),
        tap((utilityId) => this.utilityId.set(utilityId)),
      ),
    );

    return ensureGeographicalPosition$.pipe(
      switchMap((geographicalPositionId) =>
        ensureUtility$.pipe(
          switchMap((utilityId) =>
            iif(
              () => this.detailId() != null,
              of(this.detailId()!),
              this.detailService
                .createDetail({
                  body: {
                    geographicalPositionId: geographicalPositionId!,
                    utilityId: utilityId!,
                  } as DetailRequest,
                })
                .pipe(
                  map((detailResponse) => detailResponse?.id ?? null),
                  tap((detailId) => this.detailId.set(detailId)),
                ),
            ),
          ),
        ),
      ),
      tap((detailId) => {
        if (detailId == null) throw new Error('Creazione detailId fallita');
      }),
      catchError((error) => {
        this.error.set(this._msg(error));
        throw error;
      }),
      finalize(() => this.loading.set(false)),
    );
  }

  prepareCadastralFilter(cadastralFilter: CadastralFilterRequest) {
    this.error.set(null);
    this.loading.set(true);

    return iif(
      () => this.cadastralFilterId() != null,
      of(this.cadastralFilterId()!),
      this.cadastralFilterService
        .createCadastralFilter({ body: cadastralFilter })
        .pipe(
          map((cadastralFilterResponse) => cadastralFilterResponse?.id ?? null),
          tap((cadastralFilterId) =>
            this.cadastralFilterId.set(cadastralFilterId),
          ),
        ),
    ).pipe(
      tap((cadastralFilterId) => {
        if (cadastralFilterId == null)
          throw new Error('Creazione cadastralFilterId fallita');
      }),
      catchError((error) => {
        this.error.set(this._msg(error));
        throw error;
      }),
      finalize(() => this.loading.set(false)),
    );
  }

  runFullSearch(params: {
    category: Category;
    page: number;
    size: number;
    userEmail?: string;
    geographicalPosition: GeographicalPositionRequest;
    utility: UtilityRequest;
    cadastralFilter: CadastralFilterRequest;
  }) {
    this.error.set(null);
    this.loading.set(true);

    return this.prepareDetail(params.geographicalPosition, params.utility).pipe(
      switchMap(() => this.prepareCadastralFilter(params.cadastralFilter)),
      switchMap(() =>
        this.search({
          category: params.category,
          page: params.page,
          size: params.size,
          userEmail: params.userEmail,
        }),
      ),
      finalize(() => this.loading.set(false)),
    );
  }

  resetContext() {
    this.geographicalPositionId.set(null);
    this.utilityId.set(null);
    this.detailId.set(null);
    this.cadastralFilterId.set(null);
    this._lastForm.set(null);
  }

  /** Solo RxJS: esegue la ricerca e aggiorna lo stato interno */
  search(params: {
    category: Category;
    page: number;
    size: number;
    userEmail?: string;
  }) {
    this.error.set(null);

    const guard$ = defer(() => {
      if (!this.detailId() || !this.cadastralFilterId()) {
        throw new Error(
          'Mancano gli ID necessari (detailId o cadastralFilterId).',
        );
      }
      return of(true);
    });

    const resolvedEmail =
      (params.userEmail && params.userEmail.trim()) ||
      this._currentEmail() ||
      '';

    const requestedCategory = params.category;

    const body: SearchRequest = {
      category: requestedCategory,
      page: params.page,
      size: params.size,
      detailId: this.detailId()!,
      cadastralFilterId: this.cadastralFilterId()!,
    };

    this.loading.set(true);
    this._lastForm.set({ ...params, userEmail: resolvedEmail });

    console.log('[SEARCH] body', body);
    return guard$.pipe(
      switchMap(() => this.searchService.createSearch({ body })),
      map((list) =>
        (list ?? []).map((response: any) => {
          const geographicalPosition =
            response?.geographicalPosition ??
            response?.geographicalPositionResponse ??
            {};

          const latRaw =
            response?.latitude ??
            geographicalPosition?.latitude ??
            response?.lat;

          const lonRaw =
            response?.longitude ??
            geographicalPosition?.longitude ??
            response?.lon;

          const lat = Number(latRaw);
          const lon = Number(lonRaw);

          const category = this._normalizeCategory(
            response?.category ??
              response?.realEstateCategory ??
              response?.adCategory,
          );

          return {
            ...response,
            category,
            title: response.title ?? `Immobile #${response.id}`,
            address: response.address ?? geographicalPosition?.address ?? '',
            city: response.city ?? geographicalPosition?.city ?? '',
            lat: Number.isFinite(lat) ? lat : undefined,
            lon: Number.isFinite(lon) ? lon : undefined,
          } as SearchCardGeo;
        }),
      ),

      switchMap((searchCards) => {
        const missing = searchCards.filter(
          (searchCard) =>
            !Number.isFinite(searchCard.lat as any) ||
            !Number.isFinite(searchCard.lon as any) ||
            (!searchCard.address && !searchCard.city),
        );
        if (missing.length === 0) return of(searchCards);

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
            cadastralFilter.minPrice ? `≥€${cadastralFilter.minPrice}` : '',
            cadastralFilter.maxPrice ? `≤€${cadastralFilter.maxPrice}` : '',
          ]
            .filter(Boolean)
            .join(' · ');
          const snapshot: RecentSearchSnapshot = {
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
          this._persistRecent(snapshot);
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

  nextPage() {
    const lastForm = this._lastForm();
    if (!lastForm) return of(null);
    return this.search({ ...lastForm, page: Math.max(1, lastForm.page + 1) });
  }

  prevPage() {
    const lastForm = this._lastForm();
    if (!lastForm) return of(null);
    return this.search({ ...lastForm, page: Math.max(1, lastForm.page - 1) });
  }

  private _msg(error: any): string {
    if (error?.error?.message) return error.error.message;
    if (error?.message) return error.message;
    return 'Errore inatteso';
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

  private _normalizeCategory(v: any): Category | null {
    const s = (v ?? '').toString().trim().toUpperCase();
    return s === 'SALE' ? 'SALE' : s === 'RENT' ? 'RENT' : null;
  }

  private _isRequestedCategory(
    c: { category?: Category | null },
    req: Category,
  ): c is { category: Category } {
    return this._normalizeCategory(c.category) === req;
  }
}
