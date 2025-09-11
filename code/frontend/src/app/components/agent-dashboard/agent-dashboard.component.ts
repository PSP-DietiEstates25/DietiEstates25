import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import {
  AgentService,
  Ad,
  Visit,
  Offer,
  VisitStatus,
  OfferStatus,
} from '../../vecchioService/agent.service';

@Component({
  selector: 'app-agent-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterLinkActive],
  templateUrl: './agent-dashboard.component.html',
})
export class AgentDashboardComponent {
  private api = inject(AgentService);
  private router = inject(Router);

  // Tabs
  tabs: Array<{ key: 'visits' | 'ads' | 'offers'; label: string }> = [
    { key: 'visits', label: 'Visite' },
    { key: 'ads', label: 'Annunci' },
    { key: 'offers', label: 'Offerte' },
  ];
  active = signal<'visits' | 'ads' | 'offers'>('visits');

  // Lists + loading
  visits = signal<Visit[]>([]);
  visitsLoading = signal(false);
  visitFilter = signal<VisitStatus | ''>('');
  ads = signal<Ad[]>([]);
  adsLoading = signal(false);
  offers = signal<Offer[]>([]);
  offersLoading = signal(false);
  offerFilter = signal<OfferStatus | ''>('');

  constructor() {
    this.loadVisits();
  }

  setTab(t: 'visits' | 'ads' | 'offers') {
    this.active.set(t);
    if (t === 'visits' && !this.visits().length) this.loadVisits();
    if (t === 'ads' && !this.ads().length) this.loadAds();
    if (t === 'offers' && !this.offers().length) this.loadOffers();
  }

  // VISITS
  loadVisits() {
    this.visitsLoading.set(true);
    const st = this.visitFilter();
    this.api.listVisits(st || undefined).subscribe({
      next: (v) => this.visits.set(v ?? []),
      error: () => this.visits.set([]),
      complete: () => this.visitsLoading.set(false),
    });
  }
  approveVisit(v: Visit) {
    this.api
      .updateVisitStatus(v.id, 'APPROVED')
      .subscribe(() => this.loadVisits());
  }
  declineVisit(v: Visit) {
    this.api
      .updateVisitStatus(v.id, 'DECLINED')
      .subscribe(() => this.loadVisits());
  }

  // ADS
  loadAds() {
    this.adsLoading.set(true);
    this.api.listAds().subscribe({
      next: (a) => this.ads.set(a ?? []),
      error: () => this.ads.set([]),
      complete: () => this.adsLoading.set(false),
    });
  }
  goToCreateAd() {
    this.router.navigate(['/agent/ads/new']);
  }

  // OFFERS
  loadOffers() {
    this.offersLoading.set(true);
    const st = this.offerFilter();
    this.api.listOffers(st || undefined).subscribe({
      next: (o) => this.offers.set(o ?? []),
      error: () => this.offers.set([]),
      complete: () => this.offersLoading.set(false),
    });
  }
  acceptOffer(o: Offer) {
    this.api.respondOffer(o.id, 'ACCEPT').subscribe(() => this.loadOffers());
  }
  declineOffer(o: Offer) {
    this.api.respondOffer(o.id, 'DECLINE').subscribe(() => this.loadOffers());
  }

  // counter-offer inline
  counterId = signal<number | null>(null);
  counterAmount = signal<number | null>(null);
  counterMessage = signal('');
  startCounter(o: Offer) {
    this.counterId.set(o.id);
    this.counterAmount.set(o.amount);
    this.counterMessage.set('');
  }
  cancelCounter() {
    this.counterId.set(null);
    this.counterAmount.set(null);
    this.counterMessage.set('');
  }
  sendCounter() {
    const id = this.counterId();
    const amount = this.counterAmount();
    if (!id || !amount || amount <= 0) return;
    this.api.counterOffer(id, amount, this.counterMessage()).subscribe(() => {
      this.cancelCounter();
      this.loadOffers();
    });
  }
}
