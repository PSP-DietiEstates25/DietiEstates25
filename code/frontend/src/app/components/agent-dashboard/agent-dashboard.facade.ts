import { Injectable, signal } from '@angular/core';
import {
  RealEstateControllerService,
  VisitControllerService,
  OfferControllerService,
} from '../../services/services';
import { OfferRequest } from '../../services/models';
import { forkJoin, from, of, EMPTY, concat, defer, Observable } from 'rxjs';
import {
  map,
  tap,
  catchError,
  finalize,
  take,
  switchMap,
} from 'rxjs/operators';

export type VisitVM = {
  id: number;
  adTitle: string;
  requesterName: string;
  requestedAt: string | null;
  preferredDate?: string | null;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED' | string;
};

export type OfferVM = {
  id: number;
  realEstateId: number | null;
  adTitle: string;
  bidderName: string;
  createdAt: string | null;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED' | string;
  amount?: number | null;
};

export type AdVM = {
  id: number;
  title: string;
  city: string | null;
  price: number | null;
  createdAt: string | null;
  images?: string[];
  coverSrc?: string | null;
};

const DEMO_AD: AdVM = {
  id: 999999,
  title: 'Demo — Bilocale ristrutturato in centro',
  city: 'Napoli',
  price: 250_000,
  createdAt: new Date().toISOString(),
};

@Injectable({ providedIn: 'root' })
export class AgentDashboardFacade {
  // stato visite
  visits = signal<VisitVM[]>([]);
  visitsLoading = signal(false);
  visitFilter = signal<'' | 'PENDING' | 'ACCEPTED' | 'REJECTED'>('');

  // stato annunci
  ads = signal<AdVM[]>([]);
  adsLoading = signal(false);

  // stato offerte
  offers = signal<OfferVM[]>([]);
  offersLoading = signal(false);
  offerFilter = signal<'' | 'PENDING' | 'ACCEPTED' | 'REJECTED'>('');

  // contro-offerta
  counterId = signal<number | null>(null);
  counterAmount = signal<number | null>(null);
  counterRealEstateId = signal<number | null>(null);
  counterMessage = signal('');

  // inserimento offerta "esterna" (manuale)
  addOfferForId = signal<number | null>(null);
  addOfferAmount = signal<number | null>(null);
  addOfferEmail = signal<string>('');
  addOfferLoading = signal<boolean>(false);

  constructor(
    private estateApi: RealEstateControllerService,
    private visitApi: VisitControllerService,
    private offerApi: OfferControllerService
  ) {}

  private callById$<T>(
    svc: any,
    methodNames: string[],
    id: number | string,
    payload?: any
  ): Observable<T> {
    const attempts: Observable<T>[] = [];
    for (const name of methodNames) {
      const fn = svc?.[name];
      if (typeof fn !== 'function') continue;

      if (payload !== undefined) {
        attempts.push(
          defer(
            () => fn.call(svc, { id, body: payload }) as Observable<T>
          ).pipe(catchError(() => EMPTY))
        );
        attempts.push(
          defer(() => fn.call(svc, { id, ...payload }) as Observable<T>).pipe(
            catchError(() => EMPTY)
          )
        );
      }
      attempts.push(
        defer(() => fn.call(svc, { id }) as Observable<T>).pipe(
          catchError(() => EMPTY)
        )
      );
      attempts.push(
        defer(() => fn.call(svc, id) as Observable<T>).pipe(
          catchError(() => EMPTY)
        )
      );
    }
    return attempts.length
      ? concat(...attempts).pipe(take(1))
      : defer(() => {
          throw new Error(`Metodo-byId non trovato: ${methodNames.join(', ')}`);
        });
  }

  private val<T>(o: any, keys: string[], fallback: T): T {
    for (const k of keys) if (o?.[k] != null) return o[k] as T;
    return fallback;
  }

  // Mappers
  private toVisitVM(v: any): VisitVM {
    const re = v?.realEstate ?? v?.estate ?? null;
    const reId = v?.realEstateId ?? re?.id ?? null;

    const adTitle =
      v?.adTitle ??
      re?.title ??
      re?.name ??
      (reId != null ? `Annuncio #${reId}` : 'Annuncio');

    const user = v?.user ?? v?.requester ?? v?.buyer ?? null;
    const requesterName =
      v?.requesterName ??
      user?.name ??
      user?.fullName ??
      user?.email ??
      'Utente';

    return {
      id: Number(v?.id ?? 0),
      adTitle,
      requesterName,
      requestedAt: v?.requestedAt ?? v?.createdAt ?? v?.createdDate ?? null,
      preferredDate: v?.preferredDate ?? v?.date ?? null,
      status: (v?.status ?? v?.proposalStatus ?? 'PENDING') as any,
    };
  }

  private toOfferVM(o: any, parentRealEstateId?: number): OfferVM {
    const re = o?.realEstate ?? o?.estate ?? null;
    const reId = o?.realEstateId ?? parentRealEstateId ?? re?.id ?? null;
    const title =
      o?.adTitle ??
      re?.title ??
      re?.name ??
      (reId != null ? `Annuncio #${reId}` : 'Annuncio');

    return {
      id: Number(o?.id ?? 0),
      realEstateId: reId,
      adTitle: title,
      bidderName: o?.userName ?? o?.userEmail ?? 'utente',
      createdAt: o?.createdAt ?? o?.createdDate ?? null,
      status: o?.status ?? 'PENDING',
      amount: o?.amount ?? null,
    };
  }

  private toAdVM(re: any): AdVM {
    const location = this.val<any>(
      re,
      ['geographicalPosition', 'location', 'address'],
      null
    );

    const title =
      this.val<string>(re, ['title', 'name'], '') ||
      this.val<string>(re, ['description'], 'Annuncio');

    const city =
      this.val<string>(re, ['city'], '') ||
      this.val<string>(location, ['city'], '');

    const price = this.val<number | null>(re, ['price', 'amount'], null);

    const createdAt = this.val<string>(
      re,
      ['createdAt', 'createdDate', 'lastModifiedDate'],
      ''
    );

    const images = this.val<string[]>(re, ['images'], []) ?? [];
    const first = images?.[0] ?? null;
    const coverSrc = first ? `data:image/jpeg;base64,${first}` : null;

    return {
      id: Number(this.val<number>(re, ['id'], 0)),
      title,
      city,
      price,
      createdAt,
      images,
      coverSrc,
    };
  }

  // VISITS
  loadVisits(): Observable<void> {
    this.visitsLoading.set(true);
    return this.estateApi.listAllRealEstates().pipe(
      switchMap((res) => {
        const estates = res ?? [];
        if (!estates.length) return of([] as VisitVM[]);
        // una chiamata per ogni annuncio, poi flattiamo
        return forkJoin(
          estates.map((re) =>
            this.visitApi
              .listVisitsForRealEstate({ realestateid: re.id as number })
              .pipe(
                catchError(() => of([])), // non bloccare per un annuncio in errore
                map((visits) => (visits ?? []).map((v) => this.toVisitVM(v)))
              )
          )
        ).pipe(map((chunks) => chunks.flat()));
      }),
      tap((all) => {
        const f = this.visitFilter();
        this.visits.set(f ? all.filter((v) => v.status === f) : all);
      }),
      catchError((e) => {
        console.error('[Facade] loadVisits error', e);
        this.visits.set([]);
        return of(void 0);
      }),
      finalize(() => this.visitsLoading.set(false)),
      map(() => void 0)
    );
  }

  approveVisit(v: VisitVM): Observable<void> {
    const prev = this.visits();
    this.visits.set(
      prev.map((x) => (x.id === v.id ? { ...x, status: 'ACCEPTED' } : x))
    );

    return this.visitApi.accept({ id: v.id }).pipe(
      switchMap(() => this.loadVisits()),
      catchError((e) => {
        console.error('[Facade] approveVisit', e);
        this.visits.set(prev);
        return of(void 0);
      })
    );
  }

  declineVisit(v: VisitVM): Observable<void> {
    const prev = this.visits();
    this.visits.set(
      prev.map((x) => (x.id === v.id ? { ...x, status: 'REJECTED' } : x))
    );

    return this.visitApi.reject({ id: v.id }).pipe(
      switchMap(() => this.loadVisits()),
      catchError((e) => {
        console.error('[Facade] declineVisit', e);
        this.visits.set(prev);
        return of(void 0);
      })
    );
  }

  // ADS
  loadAds(): Observable<void> {
    this.adsLoading.set(true);
    return this.estateApi.listAllRealEstates().pipe(
      map((list) => (list ?? []).map((re) => this.toAdVM(re))),
      tap((v) => this.ads.set(v)),
      catchError((e) => {
        console.error('[Facade] loadAds error', e);
        this.ads.set([]);
        return of(void 0);
      }),
      finalize(() => this.adsLoading.set(false)),
      map(() => void 0)
    );
  }

  deleteAd(adId: number): Observable<void> {
    const prev = this.ads();
    this.ads.set(prev.filter((a) => a.id !== adId));

    return this.estateApi.deleteRealEstate({ realestateid: adId }).pipe(
      catchError((e) => {
        console.error('[Facade] deleteAd error (delete)', e);
        this.ads.set(prev);
        return of(void 0);
      }),
      switchMap(() =>
        this.loadAds().pipe(
          catchError((e) => {
            console.error('[Facade] deleteAd error (reload)', e);
            return of(void 0);
          })
        )
      ),
      map(() => void 0)
    );
  }

  updateAd(
    adId: number,
    patch: Partial<{ description: string }>
  ): Observable<void> {
    const body: any = { ...patch };
    return this.estateApi.updateRealEstate({ realestateid: adId, body }).pipe(
      switchMap(() => this.loadAds()),
      catchError((e) => {
        console.error('[Facade] updateAd error', e);
        return of(void 0);
      })
    );
  }

  // OFFERS
  loadOffers(): Observable<void> {
    this.offersLoading.set(true);
    return this.estateApi.listAllRealEstates().pipe(
      switchMap((res) => {
        const estates = res ?? [];
        if (!estates.length) return of([] as OfferVM[]);
        return forkJoin(
          estates.map((re) =>
            this.offerApi
              .listOffersForRealEstate({ realestateid: re.id as number })
              .pipe(
                catchError(() => of([])),
                map((offers) => (offers ?? []).map((o) => this.toOfferVM(o)))
              )
          )
        ).pipe(map((chunks) => chunks.flat()));
      }),
      tap((all) => {
        const f = this.offerFilter();
        this.offers.set(f ? all.filter((o) => o.status === f) : all);
      }),
      catchError((e) => {
        console.error('[Facade] loadOffers error', e);
        this.offers.set([]);
        return of(void 0);
      }),
      finalize(() => this.offersLoading.set(false)),
      map(() => void 0)
    );
  }

  acceptOffer(o: OfferVM): Observable<void> {
    if (o.realEstateId == null) return of(void 0);

    const prev = this.offers();
    this.offers.set(
      prev.map((x) => (x.id === o.id ? { ...x, status: 'ACCEPTED' } : x))
    );

    return this.offerApi
      .accept1({ realestateid: o.realEstateId, id: o.id })
      .pipe(
        switchMap(() => this.loadOffers()),
        catchError((e) => {
          console.error('[Facade] acceptOffer', e);
          this.offers.set(prev);
          return of(void 0);
        })
      );
  }

  declineOffer(o: OfferVM): Observable<void> {
    if (o.realEstateId == null) return of(void 0);

    const prev = this.offers();
    this.offers.set(
      prev.map((x) => (x.id === o.id ? { ...x, status: 'REJECTED' } : x))
    );

    return this.offerApi
      .reject1({ realestateid: o.realEstateId, id: o.id })
      .pipe(
        switchMap(() => this.loadOffers()),
        catchError((e) => {
          console.error('[Facade] declineOffer', e);
          this.offers.set(prev);
          return of(void 0);
        })
      );
  }

  startAddOfferFor(adId: number) {
    this.addOfferForId.set(this.addOfferForId() === adId ? null : adId);
    if (this.addOfferForId() != null) {
      this.addOfferAmount.set(null);
      this.addOfferEmail.set('');
    }
  }

  cancelAddOffer() {
    this.addOfferForId.set(null);
    this.addOfferAmount.set(null);
    this.addOfferEmail.set('');
  }
  createExternalOffer(): Observable<void> {
    const adId = this.addOfferForId();
    const amount = this.addOfferAmount();
    const email = (this.addOfferEmail() || '').trim();

    if (adId == null || amount == null || isNaN(amount) || !email) {
      return of(void 0);
    }

    this.addOfferLoading.set(true);

    const body: OfferRequest = {
      amount,
      status: 'PENDING',
      userEmail: email,
      category: 'OFFER',
    };

    return this.offerApi.createOffer({ realestateid: adId, body }).pipe(
      catchError((e) => {
        console.error('[Facade] createExternalOffer error', e);
        return of(void 0);
      }),
      switchMap(() => this.loadOffers()),
      finalize(() => {
        this.addOfferLoading.set(false);
        this.cancelAddOffer();
      })
    );
  }

  // Counter-offer
  startCounter(o: OfferVM) {
    this.counterId.set(o.id);
    this.counterAmount.set(o.amount ?? null);
    this.counterRealEstateId.set(o.realEstateId ?? null);
    this.counterMessage.set('');
  }

  cancelCounter() {
    this.counterId.set(null);
    this.counterAmount.set(null);
    this.counterMessage.set('');
  }

  sendCounter(): Observable<void> {
    const id = this.counterId();
    const realestateid = this.counterRealEstateId();
    const amount = this.counterAmount();
    const message = (this.counterMessage() || '').trim();

    if (!id || !realestateid || !amount || amount <= 0) return of(void 0);

    return this.offerApi
      .counter({ realestateid, id, body: { amount, message } })
      .pipe(
        switchMap(() => {
          this.cancelCounter();
          return this.loadOffers();
        }),
        catchError((e) => {
          console.error('[Facade] sendCounter error', e);
          return of(void 0);
        })
      );
  }
}
