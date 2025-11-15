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
  realEstateId: number;
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
    private realEstateService: RealEstateControllerService,
    private visitService: VisitControllerService,
    private offerService: OfferControllerService
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
  private toVisitVM(visit: any): VisitVM {
    const realEstate = visit?.realEstate ?? visit?.estate ?? null;
    const realEstateId = visit?.realEstateId ?? realEstate?.id ?? 0;

    const adTitle =
      visit?.adTitle ??
      realEstate?.title ??
      realEstate?.name ??
      (realEstateId ? `Annuncio #${realEstateId}` : 'Annuncio');

    const user = visit?.user ?? visit?.requester ?? visit?.buyer ?? null;
    const requesterName =
      visit?.requesterName ??
      user?.name ??
      user?.fullName ??
      user?.email ??
      'Utente';

    return {
      id: Number(visit?.id ?? 0),
      realEstateId: Number(realEstateId),
      adTitle,
      requesterName,
      requestedAt: visit?.requestedAt ?? visit?.createdAt ?? visit?.createdDate ?? null,
      preferredDate: visit?.preferredDate ?? visit?.date ?? null,
      status: (visit?.status ?? visit?.proposalStatus ?? 'PENDING') as any,
    };
  }

  private toOfferVM(offer: any, parentRealEstateId?: number): OfferVM {
    const realEstate = offer?.realEstate ?? offer?.estate ?? null;
    const realEstateId = offer?.realEstateId ?? parentRealEstateId ?? re?.id ?? null;
    const title =
      offer?.adTitle ??
      realEstate?.title ??
      realEstate?.name ??
      (realEstateId != null ? `Annuncio #${realEstateId}` : 'Annuncio');

    return {
      id: Number(offer?.id ?? 0),
      realEstateId: realEstateId,
      adTitle: title,
      bidderName: offer?.userName ?? offer?.userEmail ?? 'utente',
      createdAt: offer?.createdAt ?? offer?.createdDate ?? null,
      status: offer?.status ?? 'PENDING',
      amount: offer?.amount ?? null,
    };
  }

  private toAdVM(realEstate: any): AdVM {
    const location = this.val<any>(
      realEstate,
      ['geographicalPosition', 'location', 'address'],
      null
    );

    const title =
      this.val<string>(realEstate, ['title', 'name'], '') ||
      this.val<string>(realEstate, ['description'], 'Annuncio');

    const city =
      this.val<string>(realEstate, ['city'], '') ||
      this.val<string>(location, ['city'], '');

    const price = this.val<number | null>(realEstate, ['price', 'amount'], null);

    const createdAt = this.val<string>(
      realEstate,
      ['createdAt', 'createdDate', 'lastModifiedDate'],
      ''
    );

    const images = this.val<string[]>(realEstate, ['images'], []) ?? [];
    const first = images?.[0] ?? null;
    const coverSrc = first ? `data:image/jpeg;base64,${first}` : null;

    return {
      id: Number(this.val<number>(realEstate, ['id'], 0)),
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
    return this.realEstateService.getRealEstates({ page: 0, size: 100 }).pipe(
      map((page) => (Array.isArray(page?.content) ? page.content : [])),
      switchMap((realEstates) => {
        if (!realEstates.length) return of([] as VisitVM[]);
        return forkJoin(
          realEstates.map((realEstate) =>
            this.visitService.getRealEstateVisits({
                realestateid: realEstate.id as number,
                page: 0,
                size: 100,
              })
              .pipe(
                catchError(() => of({ content: [] } as any)),
                map((page) =>
                  Array.isArray(page?.content) ? page.content : []
                ),
                map((visits) => visits.map((visit: any) => this.toVisitVM(visit)))
              )
          )
        ).pipe(map((chunks) => chunks.flat()));
      }),
      tap((visits) => {
        const visitFilter = this.visitFilter();
        this.visits.set(visitFilter ? visits.filter((visit) => visit.status === visitFilter) : visits);
      }),
      catchError((error) => {
        console.error('[Facade] loadVisits error', error);
        this.visits.set([]);
        return of(void 0);
      }),
      finalize(() => this.visitsLoading.set(false)),
      map(() => void 0)
    );
  }

  approveVisit(visit: VisitVM): Observable<void> {
    const prev = this.visits();
    this.visits.set(
      prev.map((visitMap) => (visitMap.id === visit.id ? { ...visitMap, status: 'ACCEPTED' } : visitMap))
    );

    return this.visitService.updateVisitStatus({
        realestateid: visit.realEstateId,
        visitid: visit.id,
        body: { status: 'ACCEPTED' } as any,
      })
      .pipe(
        switchMap(() => this.loadVisits()),
        catchError((error) => {
          console.error('[Facade] approveVisit', error);
          this.visits.set(prev);
          return of(void 0);
        })
      );
  }

  declineVisit(visit: VisitVM): Observable<void> {
    const prev = this.visits();
    this.visits.set(
      prev.map((visitMap) => (visitMap.id === visit.id ? { ...visitMap, status: 'REJECTED' } : visitMap))
    );

    return this.visitService.updateVisitStatus({
        realestateid: visit.realEstateId,
        visitid: visit.id,
        body: { status: 'REJECTED' } as any,
      })
      .pipe(
        switchMap(() => this.loadVisits()),
        catchError((error) => {
          console.error('[Facade] declineVisit', error);
          this.visits.set(prev);
          return of(void 0);
        })
      );
  }

  // ADS
  loadAds(): Observable<void> {
    this.adsLoading.set(true);
    return this.realEstateService.getRealEstates({ page: 0, size: 100 }).pipe(
      map((page) => (Array.isArray(page?.content) ? page.content : [])),
      map((list) => (list ?? []).map((realEstate) => this.toAdVM(realEstate))),
      tap((visit) => this.ads.set(visit)),
      catchError((error) => {
        console.error('[Facade] loadAds error', error);
        this.ads.set([]);
        return of(void 0);
      }),
      finalize(() => this.adsLoading.set(false)),
      map(() => void 0)
    );
  }

  deleteAd(adId: number): Observable<void> {
    const prev = this.ads();
    this.ads.set(prev.filter((ad) => ad.id !== adId));

    return this.realEstateService.deleteRealEstate({ realestateid: adId }).pipe(
      catchError((error) => {
        console.error('[Facade] deleteAd error (delete)', error);
        this.ads.set(prev);
        return of(void 0);
      }),
      switchMap(() =>
        this.loadAds().pipe(
          catchError((error) => {
            console.error('[Facade] deleteAd error (reload)', error);
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
    return this.realEstateService.updateRealEstate({ realestateid: adId, body }).pipe(
      switchMap(() => this.loadAds()),
      catchError((error) => {
        console.error('[Facade] updateAd error', error);
        return of(void 0);
      })
    );
  }

  // OFFERS
  loadOffers(): Observable<void> {
    this.offersLoading.set(true);
    return this.realEstateService.getRealEstates({ page: 0, size: 100 }).pipe(
      map((page) => (Array.isArray(page?.content) ? page.content : [])),
      switchMap((realEstates) => {
        if (!realEstates.length) return of([] as OfferVM[]);
        return forkJoin(
          realEstates.map((realEstate) =>
            this.offerService.getRealEstateOffers({
                realestateid: realEstate.id as number,
                page: 0,
                size: 100,
              })
              .pipe(
                catchError(() => of({ content: [] } as any)),
                map((page) =>
                  Array.isArray(page?.content) ? page.content : []
                ),
                map((offers) =>
                  offers.map((offer: any) => this.toOfferVM(offer, realEstate.id as number))
                )
              )
          )
        ).pipe(map((chunks) => chunks.flat()));
      }),
      tap((offers) => {
        const offerFilter = this.offerFilter();
        this.offers.set(offerFilter ? offers.filter((offer) => offer.status === offerFilter) : offers);
      }),
      catchError((error) => {
        console.error('[Facade] loadOffers error', error);
        this.offers.set([]);
        return of(void 0);
      }),
      finalize(() => this.offersLoading.set(false)),
      map(() => void 0)
    );
  }

  acceptOffer(offer: OfferVM): Observable<void> {
    if (offer.realEstateId == null) return of(void 0);

    const prev = this.offers();
    this.offers.set(
      prev.map((offerMap) => (offerMap.id === offer.id ? { ...offerMap, status: 'ACCEPTED' } : offerMap))
    );

    return this.offerService.updateOfferStatus({
        realestateid: offer.realEstateId,
        offerid: offer.id,
        body: { status: 'ACCEPTED' } as any,
      })
      .pipe(
        switchMap(() => this.loadOffers()),
        catchError((error) => {
          console.error('[Facade] acceptOffer', error);
          this.offers.set(prev);
          return of(void 0);
        })
      );
  }

  declineOffer(offer: OfferVM): Observable<void> {
    if (offer.realEstateId == null) return of(void 0);

    const prev = this.offers();
    this.offers.set(
      prev.map((offerMap) => (offerMap.id === offer.id ? { ...offerMap, status: 'REJECTED' } : offerMap))
    );

    return this.offerService.updateOfferStatus({
        realestateid: offer.realEstateId,
        offerid: offer.id,
        body: { status: 'REJECTED' } as any,
      })
      .pipe(
        switchMap(() => this.loadOffers()),
        catchError((error) => {
          console.error('[Facade] declineOffer', error);
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
      category: 'OFFER',
    };

    return this.offerService.createOffer({ realestateid: adId, body }).pipe(
      catchError((error) => {
        console.error('[Facade] createExternalOffer error', error);
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
  startCounter(offer: OfferVM) {
    this.counterId.set(offer.id);
    this.counterAmount.set(offer.amount ?? null);
    this.counterRealEstateId.set(offer.realEstateId ?? null);
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

    const prev = this.offers();
    this.offers.set(
      prev.map((offerMap) => (offerMap.id === id ? { ...offerMap, status: 'COUNTERED', amount } : offerMap))
    );

    return this.offerService.updateOfferStatus({
        realestateid,
        offerid: id,
        body: { status: 'COUNTERED', amount, message } as any,
      })
      .pipe(
        switchMap(() => {
          this.cancelCounter();
          return this.loadOffers();
        }),
        catchError((error) => {
          console.error('[Facade] sendCounter error', error);
          // rollback UI se fallisce
          this.offers.set(prev);
          return of(void 0);
        })
      );
  }
}
