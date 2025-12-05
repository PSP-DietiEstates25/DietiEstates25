import { Component, computed, effect, inject, Signal } from '@angular/core';
import { OffersFacade } from '../offer-page/offers.facade';
import { Offer, RealEstate } from '../../services/models';
import { OffersPaginatorService } from '../../manual_services/offers_paginator/offers-paginator.service';
import { OfferControllerService } from '../../services/services';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../../environments/environment.development';
import { FullOffer } from '../../interfaces/full-offer';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-offers-list',
  imports: [RouterLink],
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
        return 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/30 dark:text-emerald-200';
      case 'REJECTED':
        return 'bg-rose-100 text-rose-800 dark:bg-rose-900/30 dark:text-rose-200';
      case 'COUNTERED':
        return 'bg-amber-100 text-amber-800 dark:bg-amber-900/30 dark:text-amber-200';
      default:
        return 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-200';
    }
  }
}
