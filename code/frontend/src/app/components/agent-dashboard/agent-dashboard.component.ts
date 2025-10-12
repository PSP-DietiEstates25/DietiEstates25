import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import {
  AgentDashboardFacade,
  VisitVM,
  OfferVM,
} from './agent-dashboard.facade';

@Component({
  selector: 'app-agent-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './agent-dashboard.component.html',
})
export class AgentDashboardComponent {
  private router = inject(Router);
  private facade = inject(AgentDashboardFacade);

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

  // Counter-offer (stessi nomi)
  counterId = this.facade.counterId;
  counterAmount = this.facade.counterAmount;
  counterMessage = this.facade.counterMessage;

  constructor() {
    // carica la prima tab
    this.facade.loadVisits().subscribe();
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
    try {
      localStorage.removeItem('auth.token');
      localStorage.removeItem('token');
      localStorage.removeItem('userEmail');
      sessionStorage.removeItem('auth.token');
      sessionStorage.removeItem('token');
    } finally {
      this.router.navigateByUrl('auth/login');
    }
  }
}

function clearStorage() {
  localStorage.removeItem('userEmail');
  localStorage.removeItem('userRole');
  localStorage.removeItem('isAuthenticated');
}
