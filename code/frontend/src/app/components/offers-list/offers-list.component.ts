import { Component, computed, effect, inject, Signal } from '@angular/core';
import { OffersFacade } from '../offer-page/offers.facade';
import { OffersPaginatorService } from '../../manual_services/offers_paginator/offers-paginator.service';
import { OfferControllerService } from '../../services/services';
import { ToastrService } from 'ngx-toastr';
import { FullOffer } from '../../interfaces/full-offer';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-offers-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './offers-list.component.html',
  styleUrl: './offers-list.component.scss',
})
export class OffersListComponent {
  facade = inject(OffersFacade);
  offers!: Signal<FullOffer[]>;
  offerService = inject(OfferControllerService);
  offerPaginatorService = inject(OffersPaginatorService);
  toastrService = inject(ToastrService);

  constructor() {
    effect(() => {
      this.offers = computed(() => this.facade.offers());
      console.log(this.offers());
    });
  }

  badgeClass(status: string) {
    switch (status) {
      case 'ACCEPTED':
        return 'accepted_offer_badge';
      case 'REJECTED':
        return 'rejected_offer_badge';
      case 'COUNTERED':
        return 'countered_offer_badge';
      case 'COUNTER_OFFER':
        return 'counter_offer_badge';
      default:
        return 'pending_offer_badge';
    }
  }

  deletedRealEstateBadge(){
    return 'real_estate_deleted_badge';
  }
}
