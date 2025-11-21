import { inject, Injectable, signal } from '@angular/core';
import {
  RealEstateControllerService,
  VisitControllerService,
  OfferControllerService,
  DetailControllerService,
  GeographicalPositionControllerService,
  UtilityControllerService,
  CadastralDataControllerService,
} from '../../services/services';
import { CadastralData, CadastralDataRequest, OfferRequest, RealEstateResponse } from '../../services/models';
import { forkJoin, from, of, EMPTY, concat, defer, Observable } from 'rxjs';
import {
  map,
  tap,
  catchError,
  finalize,
  take,
  switchMap,
} from 'rxjs/operators';
import { 
  PageRealEstateResponse,
  CadastralDataResponse,
  DetailResponse,
  GeographicalPositionResponse,
  UtilityResponse
} from '../../services/models';
import { environment } from '../../../environments/environment';

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

  private realEstateService = inject(RealEstateControllerService);
  private cadastralDataService = inject(CadastralDataControllerService);
  private detailService = inject(DetailControllerService);
  private geographicalPositionService = inject(GeographicalPositionControllerService);
  private utilityService = inject(UtilityControllerService);
  private visitService = inject(VisitControllerService);
  private offerService = inject(OfferControllerService);

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
    const realEstateId = offer?.realEstateId ?? parentRealEstateId ?? realEstate?.id ?? null;
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

  private toAdVM(
    realEstate: RealEstateResponse | null,
    cadastralData: CadastralDataResponse | null,
    detail: DetailResponse | null,
    geographicalPosition: GeographicalPositionResponse | null,
    utility: UtilityResponse | null
  ): AdVM {

    const title = realEstate?.description?.trim() || 'Annuncio';
    const city = geographicalPosition?.city as string;
    const municipality = geographicalPosition?.municipality as string;
    const price = cadastralData?.price as number;
    const createdAt = realEstate?.createdDate as string;
    const images = realEstate?.images ?? [];
    const coverPath = realEstate?.images && realEstate.images.length > 0 ? realEstate.images[0] : null;
    const coverSrc = coverPath ? `${environment.apiBaseUrl}${coverPath}` : null;

    return {
      id: realEstate!.id as number,
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
    return this.realEstateService.getRealEstates({ page: 2, size: 6 }).pipe(
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
    return this.realEstateService
      .getRealEstates({ page: 0, size: 5 }) // o getRealEstates$Json / $Response a seconda del generato
      .pipe(
        switchMap((pageResp: PageRealEstateResponse) => {

          const content = pageResp.content ?? [];

          if (!content.length) {
            this.ads.set([]);
            return of(void 0);
          } 

          const adStreams = content.map((reaalEstate) => this.buildAdVM(reaalEstate));

          return forkJoin(adStreams).pipe(
            tap((ads) => this.ads.set(ads)),
            map(() => void 0)
          );
        }),
        catchError((err) => {
          console.error('[AgentDashboardFacade] loadAds error', err);
          this.ads.set([]);
          return of(void 0);
        }),
        finalize(() => this.adsLoading.set(false))
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
  ): Observable<RealEstateResponse> {
    const body: any = { ...patch };
    return this.realEstateService.updateRealEstate({ realestateid: adId, body })
    /*
    .pipe(
      switchMap(() => this.loadAds()),
      catchError((error) => {
        console.error('[Facade] updateAd error', error);
        return of(void 0);
      })
    );
    */
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

  private buildAdVM(realEstateResponse: RealEstateResponse): Observable<AdVM> {

    const cadastralData = this.cadastralDataService.getCadastralDataById({
      cadastraldataid: realEstateResponse.cadastralDataId as number
    });

    const detail = this.detailService.getDetailById({
      detailid: realEstateResponse.detailId as number 
    })

    return forkJoin({ cadastral: cadastralData, detail: detail }).pipe(

      switchMap(({ cadastral: cadastralData, detail: detail }): Observable<{
        cadastralData: CadastralDataResponse;
        detail: DetailResponse;
        geographicalPosition: GeographicalPositionResponse;
        utility: UtilityResponse;
      }> => {

        const geographicalPosition = this.geographicalPositionService.getGeographicalPositionById({
          geographicalpositionid: detail.geographicalPositionId as number
        });

        const utility = this.utilityService.getUtilityById({
          utilityid: detail.utilityId as number
        });

        // forkJoin con tutte le info
        return forkJoin({
          cadastralData: of(cadastralData),
          detail: of(detail),
          geographicalPosition: geographicalPosition,
          utility: utility,
        });
      }),

      map(({ cadastralData, detail, geographicalPosition, utility }) =>
        this.toAdVM(realEstateResponse, cadastralData, detail, geographicalPosition, utility)
      ),

      catchError((err) => {
        console.error('[AgentDashboardFacade] buildAdVM error', err);
        return of(this.toAdVM(realEstateResponse, null, null, null, null));
      })
    );
  }
}
