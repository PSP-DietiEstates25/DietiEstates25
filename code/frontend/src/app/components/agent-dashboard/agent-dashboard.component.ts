import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { RealEstateControllerService } from '../../services/services/real-estate-controller.service';
import { VisitControllerService } from '../../services/services/visit-controller.service';
import { OfferControllerService } from '../../services/services/offer-controller.service';

import { RealEstate } from '../../services/models/real-estate';
import { Visit } from '../../services/models/visit';
import { Offer } from '../../services/models/offer';

export type VisitVM = {
  id: number;
  adTitle: string;
  requesterName: string;
  requestedAt: string | null; // createdDate
  preferredDate?: string | null; // date
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
};

export type OfferVM = {
  id: number;
  adTitle: string;
  bidderName: string;
  createdAt: string | null; // createdDate
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
  amount?: number | null;
};

export type AdVM = {
  id: number;
  title: string;
  city: string | null;
  price: number | null;
  createdAt: string | null; // createdDate
};

@Component({
  selector: 'app-agent-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './agent-dashboard.component.html',
})
export class AgentDashboardComponent {
  // Services + Router
  private estateApi = inject(RealEstateControllerService);
  private visitApi = inject(VisitControllerService);
  private offerApi = inject(OfferControllerService);
  private router = inject(Router);

  // Tabs
  tabs: Array<{ key: 'visits' | 'ads' | 'offers'; label: string }> = [
    { key: 'visits', label: 'Visite' },
    { key: 'ads', label: 'Annunci' },
    { key: 'offers', label: 'Offerte' },
  ];
  active = signal<'visits' | 'ads' | 'offers'>('visits');

  // Stato liste + loading + filtri
  visits = signal<VisitVM[]>([]);
  visitsLoading = signal(false);
  visitFilter = signal<'' | 'PENDING' | 'ACCEPTED' | 'REJECTED'>('');

  ads = signal<AdVM[]>([]);
  adsLoading = signal(false);

  offers = signal<OfferVM[]>([]);
  offersLoading = signal(false);
  offerFilter = signal<'' | 'PENDING' | 'ACCEPTED' | 'REJECTED'>('');

  // Counter-offer
  counterId = signal<number | null>(null);
  counterAmount = signal<number | null>(null);
  counterMessage = signal('');

  constructor() {
    this.loadVisits();
  }

  setTab(t: 'visits' | 'ads' | 'offers') {
    this.active.set(t);
    if (t === 'visits' && !this.visits().length) this.loadVisits();
    if (t === 'ads' && !this.ads().length) this.loadAds();
    if (t === 'offers' && !this.offers().length) this.loadOffers();
  }

  private toVisitVM(v: Visit): VisitVM {
    const adTitle =
      (v as any).adTitle ??
      (v.realEstate as any)?.title ??
      (v.realEstate as any)?.name ??
      `Annuncio #${v.realEstate?.['id'] ?? '?'}`;

    const requesterName =
      (v as any).requesterName ??
      (v.user as any)?.name ??
      (v.user as any)?.email ??
      'Utente';

    return {
      id: v.id!,
      adTitle,
      requesterName,
      requestedAt: (v as any).requestedAt ?? v.createdDate ?? null,
      preferredDate: (v as any).preferredDate ?? (v as any).date ?? null,
      status: (v.proposalStatus as any) ?? 'PENDING',
    };
  }

  private toOfferVM(o: Offer): OfferVM {
    const adTitle =
      (o as any).adTitle ??
      (o.realEstate as any)?.title ??
      (o.realEstate as any)?.name ??
      `Annuncio #${o.realEstate?.['id'] ?? '?'}`;

    const bidderName =
      (o as any).bidderName ??
      (o.user as any)?.name ??
      (o.user as any)?.email ??
      'Utente';

    return {
      id: o.id!,
      adTitle,
      bidderName,
      createdAt: (o as any).createdAt ?? o.createdDate ?? null,
      status: (o.proposalStatus as any) ?? 'PENDING',
      amount: (o as any).amount ?? null,
    };
  }

  private toAdVM(re: RealEstate): AdVM {
    return {
      id: re['id'] as number,
      title:
        (re as any).title ??
        (re as any).name ??
        (re as any).description ??
        `Annuncio #${re['id'] ?? '?'}`,
      city:
        (re as any).city ??
        (re as any).location?.city ??
        (re as any).address?.city ??
        null,
      price: (re as any).price ?? (re as any).cost ?? null,
      createdAt:
        (re as any).createdAt ??
        (re as any).createdDate ??
        (re as any).lastModifiedDate ??
        null,
    };
  }

  // ===== VISITS =====
  loadVisits() {
    this.visitsLoading.set(true);
    this.visits.set([]);
    this.visitsLoading.set(false);
    console.warn(
      '[AgentDashboard] TODO loadVisits(): aggiungi endpoint di listing visite e mappa a VisitVM'
    );
  }
  approveVisit(v: VisitVM) {
    console.warn(
      '[AgentDashboard] TODO approveVisit(): aggiungi endpoint approve visit'
    );
  }
  declineVisit(v: VisitVM) {
    console.warn(
      '[AgentDashboard] TODO declineVisit(): aggiungi endpoint decline visit'
    );
  }

  // ===== ADS =====
  loadAds() {
    this.adsLoading.set(true);
    this.estateApi.getRealEstates().subscribe({
      next: (list) => {
        this.ads.set((list || []).map((re) => this.toAdVM(re as any)));
        this.adsLoading.set(false);
      },
      error: () => this.adsLoading.set(false),
    });
  }
  goToCreateAd() {
    this.router.navigate(['/agent/ads/new']);
  }

  // ===== OFFERS =====
  loadOffers() {
    this.offersLoading.set(true);
    // TODO: anche per le offerte
    this.offers.set([]);
    this.offersLoading.set(false);
    console.warn(
      '[AgentDashboard] TODO loadOffers(): aggiungi endpoint di listing offerte e mappa a OfferVM'
    );
  }
  acceptOffer(o: OfferVM) {
    console.warn(
      '[AgentDashboard] TODO acceptOffer(): aggiungi endpoint accept offer'
    );
  }
  declineOffer(o: OfferVM) {
    console.warn(
      '[AgentDashboard] TODO declineOffer(): aggiungi endpoint decline offer'
    );
  }

  // Counter-offer (placeholder)
  startCounter(o: OfferVM) {
    this.counterId.set(o.id);
    this.counterAmount.set(o.amount ?? null);
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
    console.warn(
      '[AgentDashboard] TODO sendCounter(): aggiungi endpoint counter-offer'
    );
  }
}
