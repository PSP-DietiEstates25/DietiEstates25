import {
  Component,
  inject,
  signal,
  DestroyRef,
  OnDestroy,
  effect,
  ChangeDetectorRef,
} from '@angular/core';

import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CreateAdFacade } from '../create-ad/create-ad.facade';
import {
  AgentDashboardFacade,
} from './agent-dashboard.facade';
import { AuthService } from '../../manual_services/auth/auth.service';
import { OffersPaginatorComponent } from '../offers-paginator/offers-paginator.component';
import { AgentOffersListComponent } from '../agent-offers-list/agent-offers-list.component';
import { OffersPaginatorService } from '../../manual_services/offers_paginator/offers-paginator.service';
import { ToastrService } from 'ngx-toastr';
import { OfferResponse, VisitResponse } from '../../services/models';
import { HttpErrorResponse } from '@angular/common/http';
import { FullOffer } from '../../interfaces/full-offer';
import { OfferPaginatorRequest } from '../../interfaces/offer-paginator-request';
import { AgentVisitsListComponent } from '../agent-visits-list/agent-visits-list.component';
import { VisitsPaginatorComponent } from '../visits-paginator/visits-paginator.component';
import { VisitPaginatorRequest } from '../../interfaces/visit-paginator-request';
import { VisitControllerService } from '../../services/services';
import { VisitPaginatorService } from '../../manual_services/visit_paginator/visit-paginator.service';
import { AgentAdsListComponent } from '../agent-ads-list/agent-ads-list.component';
import { AdsPaginatorComponent } from '../ads-paginator/ads-paginator.component';
import { AdsPaginatorService } from '../../manual_services/ads_paginator/ads-paginator.service';
import { FullRealEstate } from '../../interfaces/full-real-estate';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { LocalStorageService } from '../../manual_services/local-storage/local-storage.service';

@Component({
  selector: 'app-agent-dashboard',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink,
    AgentOffersListComponent,
    OffersPaginatorComponent,
    AgentVisitsListComponent,
    VisitsPaginatorComponent,
    AgentAdsListComponent,
    AdsPaginatorComponent
],
  templateUrl: './agent-dashboard.component.html',
})
export class AgentDashboardComponent implements OnDestroy {
  facade = inject(AgentDashboardFacade);
  offerPaginatorService = inject(OffersPaginatorService);
  visitPaginatorService = inject(VisitPaginatorService);
  adsPaginatorService = inject(AdsPaginatorService);
  localStorageService = inject(LocalStorageService);
  routerService = inject(Router);
  toastrService = inject(ToastrService);

  private readonly authService = inject(AuthService);

  createAdFacade = inject(CreateAdFacade);

  isAuthenticated = false;
  email = '';

  offers = this.facade.offers;
  visits = this.facade.visits;
  realEstates = this.facade.realEstates;

  offerPaginatorRequest!: OfferPaginatorRequest;
  visitPaginatorRequest!: VisitPaginatorRequest;
  adsPaginatorRequest!: PaginatorRequest;

  totalPages!: number;
  page!: number;

  // Tabs
  tabs: Array<{ key: 'visits' | 'ads' | 'offers'; label: string }> = [
    { key: 'visits', label: 'Visite' },
    { key: 'ads', label: 'Annunci' },
    { key: 'offers', label: 'Offerte' },
  ];
  active = signal<'visits' | 'ads' | 'offers'>('visits');

  // Stato esposto
  visitsLoading = this.facade.visitsLoading;
  visitFilter!: 'PENDING' | 'ACCEPTED' | 'REJECTED' | null;

  realEstatesLoading = this.facade.realEstatesLoading;

  offersLoading = this.facade.offersLoading;
  offerFilter!: 'PENDING' | 'ACCEPTED' | 'REJECTED' | 'COUNTERED' | null;

  addOfferForId = this.facade.addOfferForId;
  addOfferAmount = this.facade.addOfferAmount;
  addOfferEmail = this.facade.addOfferEmail;
  addOfferLoading = this.facade.addOfferLoading;

  // Counter-offer (stessi nomi)
  counterId = this.facade.counterId;
  counterMessage = this.facade.counterMessage;

  constructor() {
    effect(() => {
      this.offerPaginatorRequest = this.offerPaginatorService.offerRequest();
      this.visitPaginatorRequest = this.visitPaginatorService.visitRequest();
      this.adsPaginatorRequest = this.adsPaginatorService.adsRequest();
      this.offerFilter = this.offerPaginatorRequest.status;
      this.visitFilter = this.visitPaginatorRequest.status;
      this.facade.offerFilter.set(this.offerPaginatorRequest.status || null);
      if (this.active() === 'offers') {
        this.fetchEstateAgentOffers();
      }
      if (this.active() === 'visits') {
        this.fetchEstateAgentVisits();
      }
      if (this.active() === 'ads') {
        this.fetchEstateAgentOffers();
      }
    });
  }

  setTab(t: 'visits' | 'ads' | 'offers') {
    this.active.set(t);
    if (t === 'visits' && !this.visits.length) this.fetchEstateAgentVisits();
    if (t === 'ads' && !this.realEstates.length) this.fetchEstateAgentRealEstates();
    if (t === 'offers' && !this.offers.length) this.fetchEstateAgentOffers();
  }

  fetchEstateAgentOffers() {
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

  fetchEstateAgentVisits() {
    this.facade.fetchVisits(this.visitPaginatorRequest).subscribe({
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

  fetchEstateAgentRealEstates() {
    this.facade.fetchRealEstates(this.adsPaginatorRequest).subscribe({
      next: (results) => {
        this.totalPages = results.totalPages!;
        this.realEstates = results.fullRealEstates!;
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

  onOfferFilterChange(status: string) {
    const newStatus = status === '' ? null : (status as any);
    this.offerPaginatorService.setStatus(newStatus);
  }

  onVisitFilterChange(status: string) {
    const newStatus = status === '' ? null : (status as any);
    this.visitPaginatorService.setStatus(newStatus);
  }

  // VISITS
  approveVisit(visit: VisitResponse) {
    this.facade.approveVisit(visit).subscribe();
  }

  declineVisit(visit: VisitResponse) {
    this.facade.declineVisit(visit).subscribe();
  }

  // ADS
  goToCreateAd() {
    this.routerService.navigate(['/basics']);
  }

  deleteAd(adId: number) {
    this.facade.deleteAd(adId).subscribe();
  }

  renameAd(adId: number) {
    const description = prompt('Nuova descrizione?');
    if (description && description.trim()) {
      this.facade
        .updateAd(adId, { description: description.trim() })
        .subscribe();
    }
  }

  // OFFERS
  acceptOffer(offer: FullOffer) {
    this.facade.acceptOffer(offer).subscribe({
      next: () => this.toastrService.success('Offerta accettata con successo'),
      error: () => this.toastrService.error("Errore durante l'operazione"),
    });
  }

  declineOffer(offer: FullOffer) {
    this.facade.declineOffer(offer).subscribe({
      next: () => this.toastrService.success('Offerta rifiutata'),
      error: () => this.toastrService.error("Errore durante l'operazione"),
    });
  }

  startAddOfferFor(adId: number) {
    this.facade.startAddOfferFor(adId);
  }

  cancelAddOffer() {
    this.facade.cancelAddOffer();
  }

  submitExternalOffer() {
    this.facade.createExternalOffer().subscribe();
  }

  // Counter-offer
  startCounter(offer: FullOffer) {
    this.facade.startCounter(offer);
  }

  cancelCounter() {
    this.facade.cancelCounter();
  }

  sendCounter() {
    this.facade.sendCounter().subscribe();
  }

  initPages() {
    this.offerPaginatorService.setPagesNumber(this.totalPages);
    this.page = this.offerPaginatorService.page();
  }

  ngOnDestroy(): void {
    this.offerPaginatorService.refresh();
    this.visitPaginatorService.refresh();
    this.adsPaginatorService.refresh();
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.isAuthenticated = false;
      this.email = '';

      this.localStorageService.removeItem('isAuthenticated');
      this.localStorageService.removeItem('role');

      this.routerService.navigateByUrl('/').then(() => {
         window.location.reload();
      });
    });
  }
}
