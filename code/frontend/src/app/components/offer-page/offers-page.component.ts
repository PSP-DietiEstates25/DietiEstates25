import { Component, effect, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { OffersFacade } from './offers.facade';
import { OffersPaginatorService } from '../../manual_services/offers_paginator/offers-paginator.service';
import { OfferResponse } from '../../services/models';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { OffersListComponent } from '../offers-list/offers-list.component';
import { OffersPaginatorComponent } from '../offers-paginator/offers-paginator.component';

@Component({
  selector: 'app-offers-page',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    OffersListComponent,
    OffersPaginatorComponent,
  ],
  templateUrl: './offers-page.component.html',
})
export class OffersPageComponent implements OnDestroy {
  facade = inject(OffersFacade);
  offerPaginatorService = inject(OffersPaginatorService);
  routerService = inject(Router);
  toastrService = inject(ToastrService);

  offers: OfferResponse[] = [];
  offerPaginatorRequest!: PaginatorRequest;
  totalPages!: number;
  page!: number;

  constructor() {
    effect(() => {
      this.offerPaginatorRequest = this.offerPaginatorService.offerRequest();
      this.fetchUserOffers();
    });
  }

  fetchUserOffers() {
    this.facade.fetchOffers(this.offerPaginatorRequest).subscribe({
      next: (results) => {
        this.totalPages = results.totalPages!;
        this.offers = results.content!;
        this.initPages();
      },
      error: (response: HttpErrorResponse) => {
        if (response.error === 500) {
          this.toastrService.error('Contatta un admin', 'Errore interno');
          this.routerService.navigateByUrl('/');
        }
      },
    });
  }

  initPages() {
    this.offerPaginatorService.setPagesNumber(this.totalPages);
    this.page = this.offerPaginatorService.page();
  }

  ngOnDestroy(): void {
    this.offerPaginatorService.refresh();
  }
}
