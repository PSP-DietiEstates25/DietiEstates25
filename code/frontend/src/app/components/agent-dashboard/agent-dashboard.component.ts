import { Component, inject, signal, DestroyRef } from '@angular/core';
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
import { AuthService } from '../../manual_services/auth.service';
import { environment } from '../../../environments/environment.development';

@Component({
  selector: 'app-agent-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './agent-dashboard.component.html',
})
export class AgentDashboardComponent {

  private router = inject(Router);
  private facade = inject(AgentDashboardFacade);
  private destroyRef = inject(DestroyRef);

  private readonly authService = inject(AuthService);

  createAdFacade = inject(CreateAdFacade);

  isAuthenticated = false;
  email = '';

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

  offers = this.facade.offers;
  offersLoading = this.facade.offersLoading;
  offerFilter = this.facade.offerFilter;

  addOfferForId = this.facade.addOfferForId;
  addOfferAmount = this.facade.addOfferAmount;
  addOfferEmail = this.facade.addOfferEmail;
  addOfferLoading = this.facade.addOfferLoading;

  // Counter-offer (stessi nomi)
  counterId = this.facade.counterId;
  counterAmount = this.facade.counterAmount;
  counterMessage = this.facade.counterMessage;

  constructor() {
    // carica la prima tab
    this.facade.loadVisits().subscribe();
  }

  ngOnInit(): void {
    this.facade.loadAds().subscribe({
      next: (page) => {
        console.log(page);
      }
    });

    // ricarica quando viene pubblicato un nuovo annuncio
    this.createAdFacade.published$
      .pipe(
        switchMap(() => this.facade.loadAds()),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe();
  }

  setTab(t: 'visits' | 'ads' | 'offers') {
    this.active.set(t);
    if (t === 'visits' && !this.visits().length)
      this.facade.loadVisits().subscribe();
    if (t === 'ads' && !this.ads().length) this.facade.loadAds().subscribe();
    if (t === 'offers' && !this.offers().length)
      this.facade.loadOffers().subscribe();
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
    this.router.navigate(['/agent/ads/new']);
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
  loadOffers() {
    this.facade.loadOffers().subscribe();
  }
  acceptOffer(o: OfferVM) {
    this.facade.acceptOffer(o).subscribe();
  }
  declineOffer(o: OfferVM) {
    this.facade.declineOffer(o).subscribe();
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
  startCounter(o: OfferVM) {
    this.facade.startCounter(o);
  }
  cancelCounter() {
    this.facade.cancelCounter();
  }
  sendCounter() {
    this.facade.sendCounter().subscribe();
  }

  logout() {
    this.authService.logout().subscribe(() => {
      this.isAuthenticated = false;
      this.email = '';
      this.router.navigateByUrl(`${environment.apiBaseUrl}/oauth2/authorization/messaging-client-oidc?prompt=login`);
    });
  }
}