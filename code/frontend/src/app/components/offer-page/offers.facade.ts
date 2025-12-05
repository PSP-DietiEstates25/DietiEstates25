import { Injectable, inject, signal, computed } from '@angular/core';
import {
  OfferControllerService,
  RealEstateControllerService,
} from '../../services/services';
import { RealEstateResponse } from '../../services/models/real-estate-response';
import { OfferResponse } from '../../services/models/offer-response';
import { PageOfferResponse } from '../../services/models/page-offer-response';
import { PageRealEstateResponse } from '../../services/models/page-real-estate-response';
import { Subject, from, of } from 'rxjs';
import {
  catchError,
  finalize,
  map,
  mergeMap,
  switchMap,
  takeUntil,
  toArray,
} from 'rxjs/operators';

export type OfferStatus =
  | 'PENDING'
  | 'ACCEPTED'
  | 'REJECTED'
  | 'COUNTERED'
  | string;

export interface MyOfferVM {
  id: number;
  amount: number;
  status: OfferStatus;
  createdAt: string | null;
}

export interface OfferedRealEstateVM {
  realEstate: RealEstateResponse;
  lastOffer: MyOfferVM;
}

@Injectable({ providedIn: 'root' })
export class OffersFacade {
  private realEstateService = inject(RealEstateControllerService);
  private offerService = inject(OfferControllerService);

  readonly pageSize = signal<number>(9);
  readonly pageIndex = signal<number>(0);

  readonly items = signal<OfferedRealEstateVM[]>([]);

  readonly loading = signal<boolean>(false);
  readonly scanning = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  private scanPage = 0;
  private readonly scanSize = 18;
  private readonly concurrency = 6;
  readonly done = signal<boolean>(false);

  private cancel$ = new Subject<void>();

  readonly visibleItems = computed(() => {
    const p = this.pageIndex();
    const size = this.pageSize();
    const start = p * size;
    return this.items().slice(start, start + size);
  });

  readonly canPrev = computed(() => this.pageIndex() > 0);

  readonly canNext = computed(() => {
    const p = this.pageIndex();
    const size = this.pageSize();
    const startNext = (p + 1) * size;

    return this.items().length > startNext || !this.done();
  });

  readonly pageLabel = computed(() => `Pagina ${this.pageIndex() + 1}`);

  private toMyOfferVM(offerResponse: OfferResponse): MyOfferVM {
    return {
      id: (offerResponse as any)?.id ?? 0,
      amount: (offerResponse as any)?.amount ?? 0,
      status: ((offerResponse as any)?.status ?? 'PENDING') as any,
      createdAt:
        (offerResponse as any)?.createdAt ??
        (offerResponse as any)?.createdDate ??
        null,
    };
  }

  reset() {
    this.cancel$.next();
    this.items.set([]);
    this.error.set(null);
    this.loading.set(false);
    this.scanning.set(false);
    this.done.set(false);
    this.scanPage = 0;
    this.pageIndex.set(0);
  }

  init() {
    this.reset();
    this.ensurePage(this.pageIndex());
  }

  private ensurePage(page: number) {
    const targetCount = (page + 1) * this.pageSize();

    if (this.items().length >= targetCount || this.done()) return;

    this.cancel$.next();
    this.scanning.set(true);
    this.error.set(null);

    this.scanUntil(targetCount);
  }

  private scanUntil(targetCount: number) {
    if (this.done() || this.items().length >= targetCount) {
      this.scanning.set(false);
      return;
    }

    const pageToScan = this.scanPage;
    this.loading.set(true);

    this.realEstateService
      .getRealEstates({ page: pageToScan, size: this.scanSize })
      .pipe(
        switchMap((page: PageRealEstateResponse) => {
          const realEstates: RealEstateResponse[] = Array.isArray(page?.content)
            ? (page.content as RealEstateResponse[])
            : [];

          return from(realEstates).pipe(
            mergeMap((re) => {
              const id = re?.id ?? 0;
              if (!id) return of<OfferedRealEstateVM | null>(null);

              return this.offerService
                .getRealEstateOffers({ realestateid: id, page: 0, size: 1 })
                .pipe(
                  map((offerPage: PageOfferResponse) => {
                    const offers: OfferResponse[] = Array.isArray(
                      offerPage?.content,
                    )
                      ? (offerPage.content as OfferResponse[])
                      : [];
                    if (!offers.length) return null;

                    return {
                      realEstate: re,
                      lastOffer: this.toMyOfferVM(offers[0]),
                    } satisfies OfferedRealEstateVM;
                  }),
                  catchError(() => of<OfferedRealEstateVM | null>(null)),
                );
            }, this.concurrency),
            toArray(),
            map((arr) => arr.filter(Boolean) as OfferedRealEstateVM[]),
            map((found) => ({ found, last: !!page?.last })),
          );
        }),
        takeUntil(this.cancel$),
        finalize(() => this.loading.set(false)),
      )
      .subscribe({
        next: ({ found, last }) => {
          this.scanPage += 1;
          if (last) this.done.set(true);

          const existing = new Set(
            this.items().map((x) => x.realEstate?.id ?? -1),
          );
          const newOnes = found.filter(
            (x) => !existing.has(x.realEstate?.id ?? -1),
          );

          if (newOnes.length) this.items.set([...this.items(), ...newOnes]);

          if (!this.done() && this.items().length < targetCount) {
            this.scanUntil(targetCount);
          } else {
            this.scanning.set(false);
          }
        },
        error: (e) => {
          console.error(e);
          this.error.set('Errore nel caricamento delle offerte.');
          this.scanning.set(false);
        },
      });
  }
}
