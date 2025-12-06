import {
  Component,
  inject,
  signal,
  DestroyRef,
  OnDestroy,
  effect,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CreateAdFacade } from '../create-ad/create-ad.facade';
import { switchMap } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  AgentDashboardFacade,
  VisitVM,
  OfferVM,
} from './agent-dashboard.facade';
import { AuthService } from '../../manual_services/auth/auth.service';
import { environment } from '../../../environments/environment.development';
import { OffersListComponent } from '../offers-list/offers-list.component';
import { OffersPaginatorComponent } from '../offers-paginator/offers-paginator.component';
import { AgentOffersListComponent } from '../agent-offers-list/agent-offers-list.component';
import { OffersPaginatorService } from '../../manual_services/offers_paginator/offers-paginator.service';
import { OfferControllerService } from '../../services/services';
import { ToastrService } from 'ngx-toastr';
import { OfferResponse } from '../../services/models';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { HttpErrorResponse } from '@angular/common/http';
import { FullOffer } from '../../interfaces/full-offer';
import { OfferPaginatorRequest } from '../../interfaces/offer-paginator-request';

@Component({
  selector: 'app-agent-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    AgentOffersListComponent,
    OffersPaginatorComponent,
  ],
  templateUrl: './agent-dashboard.component.html',
})
export class AgentDashboardComponent implements OnDestroy {
  facade = inject(AgentDashboardFacade);
  offerPaginatorService = inject(OffersPaginatorService);
  routerService = inject(Router);
  toastrService = inject(ToastrService);

  private destroyRef = inject(DestroyRef);
  private readonly authService = inject(AuthService);

  createAdFacade = inject(CreateAdFacade);

  isAuthenticated = false;
  email = '';

  offers: OfferResponse[] = [];
  offerPaginatorRequest!: OfferPaginatorRequest;
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
  visits = this.facade.visits;
  visitsLoading = this.facade.visitsLoading;
  visitFilter = this.facade.visitFilter;

  ads = this.facade.ads;
  adsLoading = this.facade.adsLoading;

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
    // carica la prima tab
    effect(() => {
      this.offerPaginatorRequest = this.offerPaginatorService.offerRequest();
      this.offerFilter = this.offerPaginatorRequest.status;
      this.facade.offerFilter.set(this.offerPaginatorRequest.status || null);
      if (this.active() === 'offers') {
        this.fetchEstateAgentOffers();
      }
    });
    this.facade.loadVisits().subscribe();
  }

  ngOnInit(): void {
    this.facade.loadAds().subscribe({
      next: (page) => {
        console.log(page);
      },
    });

    // ricarica quando viene pubblicato un nuovo annuncio
    this.createAdFacade.published$
      .pipe(
        switchMap(() => this.facade.loadAds()),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe();
  }

  setTab(t: 'visits' | 'ads' | 'offers') {
    this.active.set(t);
    if (t === 'visits' && !this.visits().length)
      this.facade.loadVisits().subscribe();
    if (t === 'ads' && !this.ads().length) this.facade.loadAds().subscribe();
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

  onOfferFilterChange(status: string) {
    const newStatus = status === '' ? null : (status as any);
    this.offerPaginatorService.setStatus(newStatus);
  }

  // VISITS
  loadVisits() {
    this.facade.loadVisits().subscribe();
  }
  approveVisit(v: VisitVM) {
    this.facade.approveVisit(v).subscribe();
  }
  declineVisit(v: VisitVM) {
    this.facade.declineVisit(v).subscribe();
  }

  // ADS
  loadAds() {
    this.facade.loadAds().subscribe();
  }
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
  }

  logout() {
    this.authService.logout().subscribe(() => {
      this.isAuthenticated = false;
      this.email = '';
      this.routerService.navigateByUrl(
        `${environment.apiBaseUrl}/oauth2/authorization/messaging-client-oidc?prompt=login`,
      );
    });
  }
}
