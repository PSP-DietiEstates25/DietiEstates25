import { Injectable, inject, signal } from '@angular/core';
import {
  OfferControllerService,
  RealEstateControllerService,
} from '../../services/services';
import { forkJoin, of } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { AuthService } from '../../manual_services/auth/auth.service';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { FullOffer } from '../../interfaces/full-offer';

@Injectable({ providedIn: 'root' })
export class OffersFacade {
  private offerService = inject(OfferControllerService);

  loading = signal(false);
  scanning = signal(false);
  done = signal(false);
  error = signal(false);

  offers = signal<FullOffer[]>([]);

  getOffers(request: PaginatorRequest) {
    const params = {
      size: request.size,
      page: request.page - 1,
    };
    return this.offerService.getOffers(params);
  }

  fetchOffers(request: PaginatorRequest) {
    const params = {
      size: request.size,
      page: request.page,
    };
    return this.getOffers(params).pipe(
      switchMap((response) => {
        const requests = response.content!.map((offer) => {
          const counterOffer = offer.counterOfferId
            ? this.offerService.getOfferById({
                realestateid: offer.realEstateId!,
                offerid: offer.counterOfferId!,
              })
            : of(null);

          return forkJoin({
            offer: of(offer),
            counterOffer: counterOffer,
          }).pipe(
            map((result) => {
              return {
                ...result.offer,
                counterOffer: result.counterOffer,
              };
            }),
          );
        });

        return forkJoin(requests).pipe(
          map((offerObservables) => {
            return {
              ...response,
              fullOffers: offerObservables,
            };
          }),
        );
      }),
      tap((fullOffersResponse) => {
        const newOffers: FullOffer[] = fullOffersResponse.fullOffers.map(
          (offer) => ({ ...offer }),
        );
        this.offers.set(newOffers);
      }),
    );
  }
}
