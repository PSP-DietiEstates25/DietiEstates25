import { Injectable, inject, signal, computed } from '@angular/core';
import { of, defer, iif, forkJoin } from 'rxjs';
import { map, switchMap, tap, catchError, finalize } from 'rxjs/operators';

import { SearchControllerService } from '../../services/services/search-controller.service';
import { GeographicalPositionControllerService } from '../../services/services/geographical-position-controller.service';
import { UtilityControllerService } from '../../services/services/utility-controller.service';
import { DetailControllerService } from '../../services/services/detail-controller.service';
import { CadastralFilterControllerService } from '../../services/services/cadastral-filter-controller.service';

import { RealEstateResponse } from '../../services/models/real-estate-response';
import { SearchRequest } from '../../services/models/search-request';
import { GeographicalPositionRequest } from '../../services/models/geographical-position-request';
import { UtilityRequest } from '../../services/models/utility-request';
import { DetailRequest } from '../../services/models/detail-request';
import { CadastralFilterRequest } from '../../services/models/cadastral-filter-request';

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
  geo: GeographicalPositionRequest;
  uti: UtilityRequest;
  cf: CadastralFilterRequest;
  label: string;
}

@Injectable({ providedIn: 'root' })
export class SearchFacade {
  constructor() {
    const email = this.getEmailFromLocalStorage();
    this.loadRecentFor(email);
  }
  private searches = inject(SearchControllerService);
  private geoSvc = inject(GeographicalPositionControllerService);
  private utilSvc = inject(UtilityControllerService);
  private detSvc = inject(DetailControllerService);
  private cadfSvc = inject(CadastralFilterControllerService);

  loading = signal(false);
  error = signal<string | null>(null);

  geographicalPositionId = signal<number | null>(null);
  utilityId = signal<number | null>(null);
  detailId = signal<number | null>(null);
  cadastralFilterId = signal<number | null>(null);

  private _detailCache = new Map<number, { geographicalPositionId?: number }>();
  private _geoCache = new Map<
    number,
    { latitude?: number; longitude?: number; address?: string; city?: string }
  >();

  private _lastForm = signal<{
    category: Category;
    page: number; // 1-based
    size: number; // >0
    userEmail: string;
  } | null>(null);
  lastForm = this._lastForm.asReadonly();

  cards = signal<SearchCard[]>([]);
  hasNext = computed(
    () => this.cards().length >= (this._lastForm()?.size ?? 0)
  );
  hasPrev = computed(() => (this._lastForm()?.page ?? 1) > 1);

  private getUserEmailSafe(input: string | null | undefined): string {
    const email = input && input.trim() ? input.trim() : 'vale@email.com';
    return email;
  }

  private _cachedGeoSig = signal<GeographicalPositionRequest | null>(null);
  private _cachedUtiSig = signal<UtilityRequest | null>(null);
  private _cachedCfSig = signal<CadastralFilterRequest | null>(null);

  cacheFilters(
    geo: GeographicalPositionRequest,
    uti: UtilityRequest,
    cf: CadastralFilterRequest
  ) {
    this._cachedGeoSig.set(geo ?? null);
    this._cachedUtiSig.set(uti ?? null);
    this._cachedCfSig.set(cf ?? null);
  }

  private _cachedGeo() {
    return this._cachedGeoSig();
  }
  private _cachedUti() {
    return this._cachedUtiSig();
  }
  private _cachedCf() {
    return this._cachedCfSig();
  }

  recent = signal<RecentSearchSnapshot[]>([]);

  private _recentKey(email: string | null) {
    return `recent-searches:${email ?? 'anon'}`;
  }

  loadRecentFor(email: string | null) {
    try {
      const raw = localStorage.getItem(this._recentKey(email));
      const arr: RecentSearchSnapshot[] = raw ? JSON.parse(raw) : [];
      const cleaned = (arr ?? [])
        .filter((s) => !!s?.id && !!s?.when)
        .sort((a, b) => b.when.localeCompare(a.when));
      this.recent.set(cleaned);
    } catch {
      this.recent.set([]);
    }
  }

  forgetRecent() {
    const email = this.getEmailFromLocalStorage();
    localStorage.removeItem(this._recentKey(email));
    this.recent.set([]);
  }

  private _persistRecent(s: RecentSearchSnapshot, max = 10) {
    const email = this.getEmailFromLocalStorage();
    const key = this._recentKey(email);

    const current = [...this.recent()];
    const without = current.filter((x) => x.id !== s.id);
    const next = [s, ...without].slice(0, max);

    this.recent.set(next);
    try {
      localStorage.setItem(key, JSON.stringify(next));
    } catch {}
  }

  removeRecent(id: string) {
    const email = this.getEmailFromLocalStorage();
    const key = this._recentKey(email);
    const next = this.recent().filter((s) => s.id !== id);
    this.recent.set(next);
    try {
      localStorage.setItem(key, JSON.stringify(next));
    } catch {}
  }

  replaySearch(s: RecentSearchSnapshot) {
    this.resetContext();

    this._cachedGeoSig.set(s.geo);
    this._cachedUtiSig.set(s.uti);
    this._cachedCfSig.set(s.cf);

    const email = this.getEmailFromLocalStorage() ?? s.userEmail ?? '';
    return this.runFullSearch({
      category: s.category,
      page: Math.max(1, s.page || 1),
      size: Math.max(1, s.size || 10),
      userEmail: email,
      geo: s.geo,
      uti: s.uti,
      cf: s.cf,
    }).subscribe();
  }

  private _hash(obj: unknown): string {
    const str = JSON.stringify(obj, Object.keys(obj as any).sort());
    let h = 0,
      i = 0,
      len = str.length;
    while (i < len) {
      h = (h * 31 + str.charCodeAt(i++)) | 0;
    }
    return `h${(h >>> 0).toString(16)}`;
  }

  prepareDetail(geo: GeographicalPositionRequest, uti: UtilityRequest) {
    this.error.set(null);
    this.loading.set(true);

    const ensureGeo$ = iif(
      () => this.geographicalPositionId() != null,
      of(this.geographicalPositionId()!),
      this.geoSvc.createGeographicalPosition({ body: geo }).pipe(
        map((res) => res?.id ?? null),
        tap((id) => this.geographicalPositionId.set(id))
      )
    );

    const ensureUtil$ = iif(
      () => this.utilityId() != null,
      of(this.utilityId()!),
      this.utilSvc.createUtility({ body: uti }).pipe(
        map((res) => res?.id ?? null),
        tap((id) => this.utilityId.set(id))
      )
    );

    return ensureGeo$.pipe(
      switchMap((geoId) =>
        ensureUtil$.pipe(
          switchMap((utilId) =>
            iif(
              () => this.detailId() != null,
              of(this.detailId()!),
              this.detSvc
                .createDetail({
                  body: {
                    geographicalPositionId: geoId!,
                    utilityId: utilId!,
                  } as DetailRequest,
                })
                .pipe(
                  map((res) => res?.id ?? null),
                  tap((id) => this.detailId.set(id))
                )
            )
          )
        )
      ),
      tap((detailId) => {
        if (detailId == null) throw new Error('Creazione detailId fallita');
      }),
      catchError((err) => {
        this.error.set(this._msg(err));
        throw err;
      }),
      finalize(() => this.loading.set(false))
    );
  }

  prepareCadastralFilter(cf: CadastralFilterRequest) {
    this.error.set(null);
    this.loading.set(true);

    return iif(
      () => this.cadastralFilterId() != null,
      of(this.cadastralFilterId()!),
      this.cadfSvc.createCadastralFilter({ body: cf }).pipe(
        map((res) => res?.id ?? null),
        tap((id) => this.cadastralFilterId.set(id))
      )
    ).pipe(
      tap((id) => {
        if (id == null) throw new Error('Creazione cadastralFilterId fallita');
      }),
      catchError((err) => {
        this.error.set(this._msg(err));
        throw err;
      }),
      finalize(() => this.loading.set(false))
    );
  }

  runFullSearch(params: {
    category: Category;
    page: number;
    size: number;
    userEmail?: string;
    geo: GeographicalPositionRequest;
    uti: UtilityRequest;
    cf: CadastralFilterRequest;
  }) {
    this.error.set(null);
    this.loading.set(true);

    return this.prepareDetail(params.geo, params.uti).pipe(
      switchMap(() => this.prepareCadastralFilter(params.cf)),
      switchMap(() =>
        this.search({
          category: params.category,
          page: params.page,
          size: params.size,
          userEmail: params.userEmail,
        })
      ),
      finalize(() => this.loading.set(false))
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
          'Mancano gli ID necessari (detailId o cadastralFilterId).'
        );
      }
      return of(true);
    });

    const resolvedEmail =
      (params.userEmail && params.userEmail.trim()) ||
      this.getEmailFromLocalStorage();

    if (!resolvedEmail) {
      this.error.set(
        'Email utente non trovata (localStorage/JWT). Effettua il login.'
      );
      return of(null);
    }

    const requestedCategory = params.category;

    const body: SearchRequest = {
      category: requestedCategory,
      page: params.page,
      size: params.size,
      userEmail: resolvedEmail,
      detailId: this.detailId()!,
      cadastralFilterId: this.cadastralFilterId()!,
    };

    this.loading.set(true);
    this._lastForm.set({ ...params, userEmail: resolvedEmail });

    console.log('[SEARCH] body', body);
    return guard$.pipe(
      switchMap(() => this.searches.createSearch({ body })),
      map((list) =>
        (list ?? []).map((r: any) => {
          const gp =
            r?.geographicalPosition ?? r?.geographicalPositionResponse ?? {};
          const latRaw = r?.latitude ?? gp?.latitude ?? r?.lat;
          const lonRaw = r?.longitude ?? gp?.longitude ?? r?.lon;

          const lat = Number(latRaw);
          const lon = Number(lonRaw);

          const category = this._normalizeCategory(
            r?.category ?? r?.realEstateCategory ?? r?.adCategory
          );

          return {
            ...r,
            category,
            title: r.title ?? `Immobile #${r.id}`,
            address: r.address ?? gp?.address ?? '',
            city: r.city ?? gp?.city ?? '',
            lat: Number.isFinite(lat) ? lat : undefined,
            lon: Number.isFinite(lon) ? lon : undefined,
          } as SearchCardGeo;
        })
      ),

      switchMap((cards) => {
        const missing = cards.filter(
          (c) =>
            !Number.isFinite(c.lat as any) ||
            !Number.isFinite(c.lon as any) ||
            (!c.address && !c.city)
        );
        if (missing.length === 0) return of(cards);

        const detailIds = Array.from(
          new Set(
            missing
              .map((m) => Number((m as any).detailId))
              .filter(Number.isFinite)
          )
        ) as number[];
        if (detailIds.length === 0) return of(cards);

        return forkJoin(detailIds.map((id) => this._getDetail(id))).pipe(
          switchMap((details) => {
            const mapDetailToGeo = new Map<number, number>();
            details.forEach((d, i) => {
              const did = detailIds[i];
              const gid = Number(d?.geographicalPositionId);
              if (Number.isFinite(gid)) mapDetailToGeo.set(did, gid);
            });

            const geoIds = Array.from(
              new Set(
                Array.from(mapDetailToGeo.values()).filter(Number.isFinite)
              )
            ) as number[];
            if (geoIds.length === 0) return of({ cards, mapDetailToGeo });

            return forkJoin(geoIds.map((id) => this._getGeo(id))).pipe(
              map((geos) => {
                const mapGeo = new Map<number, any>();
                geos.forEach((g, i) => mapGeo.set(geoIds[i], g));
                return { cards, mapDetailToGeo, mapGeo };
              })
            );
          }),

          map(({ cards, mapDetailToGeo, mapGeo }: any) =>
            cards.map((c: any) => {
              const did = Number(c.detailId);
              const gid = mapDetailToGeo?.get(did);
              const g = gid != null ? mapGeo?.get(gid) : null;

              const lat = Number.isFinite(c.lat) ? c.lat : Number(g?.latitude);
              const lon = Number.isFinite(c.lon) ? c.lon : Number(g?.longitude);
              const address = c.address || g?.address || '';
              const city = c.city || g?.city || '';

              return {
                ...c,
                lat: Number.isFinite(lat) ? lat : undefined,
                lon: Number.isFinite(lon) ? lon : undefined,
                address,
                city,
              } as SearchCardGeo;
            })
          )
        );
      }),

      map((cards: SearchCardWithCat[]) =>
        cards.filter((c): c is SearchCardWithCat =>
          this._isRequestedCategory(c, requestedCategory)
        )
      ),

      tap((mapped) => {
        this.cards.set(mapped);
        const geo = this._cachedGeo();
        const uti = this._cachedUti();
        const cf = this._cachedCf();
        const last = this._lastForm();
        if (geo && uti && cf && last) {
          const label = [
            geo.city || geo.municipality || geo.address || 'Ricerca',
            (last.category || '').toString(),
            cf.minPrice ? `≥€${cf.minPrice}` : '',
            cf.maxPrice ? `≤€${cf.maxPrice}` : '',
          ]
            .filter(Boolean)
            .join(' · ');
          const snapshot: RecentSearchSnapshot = {
            id: `${this._hash({
              geo,
              uti,
              cf,
              cat: last.category,
            })}-${Date.now()}`, // univoco
            when: new Date().toISOString(),
            category: last.category,
            size: last.size,
            page: last.page,
            userEmail: last.userEmail,
            geo,
            uti,
            cf,
            label,
          };
          this._persistRecent(snapshot);
        }
      }),
      catchError((err) => {
        this.error.set(this._msg(err));
        this.cards.set([]);
        throw err;
      }),
      finalize(() => this.loading.set(false))
    );
  }

  nextPage() {
    const lf = this._lastForm();
    if (!lf) return of(null);
    return this.search({ ...lf, page: Math.max(1, lf.page + 1) });
  }

  prevPage() {
    const lf = this._lastForm();
    if (!lf) return of(null);
    return this.search({ ...lf, page: Math.max(1, lf.page - 1) });
  }

  private _msg(e: any): string {
    if (e?.error?.message) return e.error.message;
    if (e?.message) return e.message;
    return 'Errore inatteso';
  }

  private getEmailFromLocalStorage(): string | null {
    try {
      const stored = localStorage.getItem('userEmail');
      if (stored && stored.trim()) return stored.trim();

      const candidates = [
        'auth.token',
        'auth_token',
        'access_token',
        'token',
        'jwt',
      ];
      let token: string | null = null;
      for (const k of candidates) {
        const v = localStorage.getItem(k);
        if (v) {
          token = v;
          break;
        }
      }
      if (!token) return null;

      token = token.replace(/^Bearer\s+/i, '').trim();

      const parts = token.split('.');
      if (parts.length < 2) return null;

      const json = this.base64UrlDecode(parts[1]);
      const payload = JSON.parse(json) as { email?: string; sub?: string };

      const email = (payload.email ?? payload.sub ?? '').trim();
      return email || null;
    } catch {
      return null;
    }
  }

  private base64UrlDecode(input: string): string {
    let str = input.replace(/-/g, '+').replace(/_/g, '/');
    const pad = str.length % 4;
    if (pad) str += '='.repeat(4 - pad);
    return atob(str);
  }

  private _getDetail(detailId: number) {
    const cached = this._detailCache.get(detailId);
    if (cached) return of(cached);
    return this.detSvc.getDetailById({ detailid: detailId }).pipe(
      map((d: any) => {
        const out = {
          geographicalPositionId:
            d?.geographicalPositionId ?? d?.geographicalPosition?.id,
        };
        this._detailCache.set(detailId, out);
        return out;
      })
    );
  }

  private _getGeo(geoId: number) {
    const cached = this._geoCache.get(geoId);
    if (cached) return of(cached);
    return this.geoSvc
      .getGeographicalPositionById({ geographicalpositionid: geoId })
      .pipe(
        map((g: any) => {
          const out = {
            latitude: Number(g?.latitude),
            longitude: Number(g?.longitude),
            address: g?.address ?? '',
            city: g?.city ?? '',
          };
          this._geoCache.set(geoId, out);
          return out;
        })
      );
  }

  private _normalizeCategory(v: any): Category | null {
    const s = (v ?? '').toString().trim().toUpperCase();
    return s === 'SALE' ? 'SALE' : s === 'RENT' ? 'RENT' : null;
  }

  private _isRequestedCategory(
    c: { category?: Category | null },
    req: Category
  ): c is { category: Category } {
    return this._normalizeCategory(c.category) === req;
  }
}
