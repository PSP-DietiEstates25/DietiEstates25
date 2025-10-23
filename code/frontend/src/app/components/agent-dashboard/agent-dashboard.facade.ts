import { Injectable, signal } from '@angular/core';
import {
  RealEstateControllerService,
  VisitControllerService,
  OfferControllerService,
} from '../../services/resource_server/services';
import { Observable, of, EMPTY, concat, defer } from 'rxjs';
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
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
};

export type OfferVM = {
  id: number;
  adTitle: string;
  bidderName: string;
  createdAt: string | null;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
  amount?: number | null;
};

export type AdVM = {
  id: number;
  title: string;
  city: string | null;
  price: number | null;
  createdAt: string | null;
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
  counterMessage = signal('');

  constructor(
    private estateApi: RealEstateControllerService,
    private visitApi: VisitControllerService,
    private offerApi: OfferControllerService
  ) {}

  private call$<T>(svc: any, methodNames: string[], arg?: any): Observable<T> {
    const attempts: Observable<T>[] = [];
    for (const name of methodNames) {
      const fn = svc?.[name];
      if (typeof fn !== 'function') continue;

      if (arg !== undefined) {
        attempts.push(
          defer(() => fn.call(svc, { body: arg }) as Observable<T>).pipe(
            catchError(() => EMPTY)
          )
        );
        attempts.push(
          defer(() => fn.call(svc, arg) as Observable<T>).pipe(
            catchError(() => EMPTY)
          )
        );
      } else {
        attempts.push(
          defer(() => fn.call(svc) as Observable<T>).pipe(
            catchError(() => EMPTY)
          )
        );
      }
    }
    return attempts.length
      ? concat(...attempts).pipe(take(1))
      : defer(() => {
          throw new Error(`Metodo non trovato: ${methodNames.join(', ')}`);
        });
  }

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
    const ad = this.val<any>(v, ['realEstate', 'ad', 'estate'], null);
    const user = this.val<any>(v, ['user', 'requester', 'buyer'], null);

    const adTitle =
      this.val<string>(v, ['adTitle'], '') ??
      this.val<string>(ad, ['title', 'name', 'description'], '') ??
      `Annuncio #${this.val<number | string>(ad, ['id'], '?')}`;

    const requesterName =
      this.val<string>(v, ['requesterName'], '') ??
      this.val<string>(user, ['name', 'fullName', 'email'], 'Utente');

    return {
      id: this.val<number>(v, ['id'], 0),
      adTitle,
      requesterName,
      requestedAt: this.val<string>(
        v,
        ['requestedAt', 'createdAt', 'createdDate'],
        ''
      ),
      preferredDate: this.val<string>(v, ['preferredDate', 'date'], ''),
      status: this.val<string>(
        v,
        ['status', 'proposalStatus'],
        'PENDING'
      ) as any,
    };
  }

  private toOfferVM(o: any): OfferVM {
    const ad = this.val<any>(o, ['realEstate', 'ad', 'estate'], null);
    const user = this.val<any>(o, ['user', 'bidder', 'buyer'], null);

    const adTitle =
      this.val<string>(o, ['adTitle'], '') ??
      this.val<string>(ad, ['title', 'name', 'description'], '') ??
      `Annuncio #${this.val<number | string>(ad, ['id'], '?')}`;

    const bidderName =
      this.val<string>(o, ['bidderName'], '') ??
      this.val<string>(user, ['name', 'fullName', 'email'], 'Utente');

    return {
      id: this.val<number>(o, ['id'], 0),
      adTitle,
      bidderName,
      createdAt: this.val<string>(o, ['createdAt', 'createdDate'], ''),
      status: this.val<string>(
        o,
        ['status', 'proposalStatus'],
        'PENDING'
      ) as any,
      amount: this.val<number | null>(o, ['amount', 'price', 'value'], null),
    };
  }

  private toAdVM(re: any): AdVM {
    const location = this.val<any>(
      re,
      ['location', 'address', 'geographicalPosition'],
      null
    );
    return {
      id: this.val<number>(re, ['id'], 0),
      title:
        this.val<string>(re, ['title', 'name'], '') ??
        this.val<string>(re, ['description'], 'Annuncio'),
      city:
        this.val<string>(re, ['city'], '') ??
        this.val<string>(location, ['city'], ''),
      price: this.val<number | null>(re, ['price', 'cost', 'amount'], null),
      createdAt: this.val<string>(
        re,
        ['createdAt', 'createdDate', 'lastModifiedDate'],
        ''
      ),
    };
  }

  // VISITS
  loadVisits(): Observable<void> {
    this.visitsLoading.set(true);
    return this.call$<any[]>(this.visitApi, [
      'getVisits',
      'getAllVisits',
      'listVisits',
      'findAll',
      'getAgentVisits',
    ]).pipe(
      map((list) => (list ?? []).map((v) => this.toVisitVM(v))),
      tap((mapped) => {
        const f = this.visitFilter();
        this.visits.set(f ? mapped.filter((v) => v.status === f) : mapped);
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
    return this.callById$(
      this.visitApi,
      ['approveVisit', 'acceptVisit', 'approve', 'accept'],
      v.id
    ).pipe(
      switchMap(() => this.loadVisits()),
      catchError((e) => {
        console.error('[Facade] approveVisit error', e);
        return of(void 0);
      })
    );
  }

  declineVisit(v: VisitVM): Observable<void> {
    return this.callById$(
      this.visitApi,
      ['rejectVisit', 'declineVisit', 'reject', 'decline'],
      v.id
    ).pipe(
      switchMap(() => this.loadVisits()),
      catchError((e) => {
        console.error('[Facade] declineVisit error', e);
        return of(void 0);
      })
    );
  }

  // ADS
  loadAds(): Observable<void> {
    this.adsLoading.set(true);
    return this.call$<any[]>(this.estateApi, [
      'getRealEstates',
      'getAllRealEstates',
      'listRealEstates',
      'findAll',
      'getAll',
    ]).pipe(
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

  // OFFERS
  loadOffers(): Observable<void> {
    this.offersLoading.set(true);
    return this.call$<any[]>(this.offerApi, [
      'getOffers',
      'getAllOffers',
      'listOffers',
      'findAll',
      'getAgentOffers',
    ]).pipe(
      map((list) => (list ?? []).map((o) => this.toOfferVM(o))),
      tap((mapped) => {
        const f = this.offerFilter();
        this.offers.set(f ? mapped.filter((o) => o.status === f) : mapped);
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
    return this.callById$(
      this.offerApi,
      ['acceptOffer', 'approveOffer', 'accept', 'approve'],
      o.id
    ).pipe(
      switchMap(() => this.loadOffers()),
      catchError((e) => {
        console.error('[Facade] acceptOffer error', e);
        return of(void 0);
      })
    );
  }

  declineOffer(o: OfferVM): Observable<void> {
    return this.callById$(
      this.offerApi,
      ['rejectOffer', 'declineOffer', 'reject', 'decline'],
      o.id
    ).pipe(
      switchMap(() => this.loadOffers()),
      catchError((e) => {
        console.error('[Facade] declineOffer error', e);
        return of(void 0);
      })
    );
  }

  // Counter-offer
  startCounter(o: OfferVM) {
    this.counterId.set(o.id);
    this.counterAmount.set(o.amount ?? null);
    this.counterMessage.set('');
  }

  cancelCounter() {
    this.counterId.set(null);
    this.counterAmount.set(null);
    this.counterMessage.set('');
  }

  sendCounter(): Observable<void> {
    const id = this.counterId();
    const amount = this.counterAmount();
    const message = this.counterMessage();
    if (!id || !amount || amount <= 0) {
      return of(void 0);
    }

    const payloads = [
      { offerId: id, amount, message },
      { id, amount, message },
      { amount, message },
    ];

    const attempts = payloads.map((p) =>
      this.callById$(
        this.offerApi,
        ['counterOffer', 'makeCounterOffer', 'proposeCounter', 'counter'],
        id,
        p
      ).pipe(catchError(() => EMPTY))
    );

    return concat(...attempts).pipe(
      take(1),
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
