import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import {
  RealEstateControllerService,
  VisitControllerService,
  OfferControllerService,
} from '../../services/services';

export type VisitVM = {
  id: number;
  adTitle: string;
  requesterName: string;
  requestedAt: string | null;
  preferredDate?: string | null;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
};

export type OfferVM = {
  id: number;
  adTitle: string;
  bidderName: string;
  createdAt: string | null;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
  amount?: number | null;
};

export type AdVM = {
  id: number;
  title: string;
  city: string | null;
  price: number | null;
  createdAt: string | null;
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

  // =========================
  // Helpers adattivi (niente modifiche a services/)
  // =========================
  private async call<T>(
    svc: any,
    methodNames: string[],
    arg?: any
  ): Promise<T> {
    for (const name of methodNames) {
      const fn = svc?.[name];
      if (typeof fn === 'function') {
        try {
          if (arg !== undefined) {
            // molti generatori: { body: ... } per POST/PUT
            try {
              return await firstValueFrom(fn.call(svc, { body: arg }));
            } catch {}
            // altri: payload diretto
            try {
              return await firstValueFrom(fn.call(svc, arg));
            } catch {}
          }
          // GET senza arg
          return await firstValueFrom(fn.call(svc));
        } catch {
          // prova il prossimo nome
        }
      }
    }
    throw new Error(
      `Metodo non trovato o invocazione fallita: ${methodNames.join(', ')}`
    );
  }

  private async callById<T>(
    svc: any,
    methodNames: string[],
    id: number | string,
    payload?: any
  ): Promise<T> {
    for (const name of methodNames) {
      const fn = svc?.[name];
      if (typeof fn === 'function') {
        try {
          // pattern più comuni
          if (payload !== undefined) {
            try {
              return await firstValueFrom(fn.call(svc, { id, body: payload }));
            } catch {}
            try {
              return await firstValueFrom(fn.call(svc, { id, ...payload }));
            } catch {}
          }
          try {
            return await firstValueFrom(fn.call(svc, { id }));
          } catch {}
          try {
            return await firstValueFrom(fn.call(svc, id));
          } catch {}
        } catch {
          // tenta il prossimo
        }
      }
    }
    throw new Error(`Metodo-byId non trovato: ${methodNames.join(', ')}`);
  }

  private val<T>(o: any, keys: string[], fallback: T): T {
    for (const k of keys) if (o?.[k] != null) return o[k] as T;
    return fallback;
  }

  // =========================
  // Mappers → VM
  // =========================
  private toVisitVM(v: any): VisitVM {
    const ad = this.val<any>(v, ['realEstate', 'ad', 'estate'], null);
    const user = this.val<any>(v, ['user', 'requester', 'buyer'], null);

    const adTitle =
      this.val<string>(v, ['adTitle'], '') ??
      this.val<string>(ad, ['title', 'name', 'description'], '') ??
      `Annuncio #${this.val<number | string>(ad, ['id'], '?')}`;

    const requesterName =
      this.val<string>(v, ['requesterName'], '') ??
      this.val<string>(user, ['name', 'fullName', 'email'], 'Utente');

    return {
      id: this.val<number>(v, ['id'], 0),
      adTitle,
      requesterName,
      requestedAt: this.val<string>(
        v,
        ['requestedAt', 'createdAt', 'createdDate'],
        ''
      ),
      preferredDate: this.val<string>(v, ['preferredDate', 'date'], ''),
      status: this.val<string>(
        v,
        ['status', 'proposalStatus'],
        'PENDING'
      ) as any,
    };
  }

  private toOfferVM(o: any): OfferVM {
    const ad = this.val<any>(o, ['realEstate', 'ad', 'estate'], null);
    const user = this.val<any>(o, ['user', 'bidder', 'buyer'], null);

    const adTitle =
      this.val<string>(o, ['adTitle'], '') ??
      this.val<string>(ad, ['title', 'name', 'description'], '') ??
      `Annuncio #${this.val<number | string>(ad, ['id'], '?')}`;

    const bidderName =
      this.val<string>(o, ['bidderName'], '') ??
      this.val<string>(user, ['name', 'fullName', 'email'], 'Utente');

    return {
      id: this.val<number>(o, ['id'], 0),
      adTitle,
      bidderName,
      createdAt: this.val<string>(o, ['createdAt', 'createdDate'], ''),
      status: this.val<string>(
        o,
        ['status', 'proposalStatus'],
        'PENDING'
      ) as any,
      amount: this.val<number | null>(o, ['amount', 'price', 'value'], null),
    };
  }

  private toAdVM(re: any): AdVM {
    const location = this.val<any>(
      re,
      ['location', 'address', 'geographicalPosition'],
      null
    );
    return {
      id: this.val<number>(re, ['id'], 0),
      title:
        this.val<string>(re, ['title', 'name'], '') ??
        this.val<string>(re, ['description'], 'Annuncio'),
      city:
        this.val<string>(re, ['city'], '') ??
        this.val<string>(location, ['city'], ''),
      price: this.val<number | null>(re, ['price', 'cost', 'amount'], null),
      createdAt: this.val<string>(
        re,
        ['createdAt', 'createdDate', 'lastModifiedDate'],
        ''
      ),
    };
  }

  // =========================
  // VISITS
  // =========================
  async loadVisits() {
    this.visitsLoading.set(true);
    try {
      const list = await this.call<any[]>(this.visitApi, [
        'getVisits',
        'getAllVisits',
        'listVisits',
        'findAll',
        'getAgentVisits',
      ]);
      const mapped = (list ?? []).map((v) => this.toVisitVM(v));
      const f = this.visitFilter();
      this.visits.set(f ? mapped.filter((v) => v.status === f) : mapped);
    } catch (e) {
      console.error('[AgentDashboard] loadVisits error', e);
      this.visits.set([]);
    } finally {
      this.visitsLoading.set(false);
    }
  }

  async approveVisit(v: VisitVM) {
    try {
      await this.callById(
        this.visitApi,
        ['approveVisit', 'acceptVisit', 'approve', 'accept'],
        v.id
      );
      this.loadVisits();
    } catch (e) {
      console.error('[AgentDashboard] approveVisit error', e);
    }
  }

  async declineVisit(v: VisitVM) {
    try {
      await this.callById(
        this.visitApi,
        ['rejectVisit', 'declineVisit', 'reject', 'decline'],
        v.id
      );
      this.loadVisits();
    } catch (e) {
      console.error('[AgentDashboard] declineVisit error', e);
    }
  }

  // =========================
  // ADS
  // =========================
  async loadAds() {
    this.adsLoading.set(true);
    try {
      const list = await this.call<any[]>(this.estateApi, [
        'getRealEstates',
        'getAllRealEstates',
        'listRealEstates',
        'findAll',
        'getAll',
      ]);
      this.ads.set((list ?? []).map((re) => this.toAdVM(re)));
    } catch (e) {
      console.error('[AgentDashboard] loadAds error', e);
      this.ads.set([]);
    } finally {
      this.adsLoading.set(false);
    }
  }

  goToCreateAd() {
    this.router.navigate(['/agent/ads/new']);
  }

  // =========================
  // OFFERS
  // =========================
  async loadOffers() {
    this.offersLoading.set(true);
    try {
      const list = await this.call<any[]>(this.offerApi, [
        'getOffers',
        'getAllOffers',
        'listOffers',
        'findAll',
        'getAgentOffers',
      ]);
      const mapped = (list ?? []).map((o) => this.toOfferVM(o));
      const f = this.offerFilter();
      this.offers.set(f ? mapped.filter((o) => o.status === f) : mapped);
    } catch (e) {
      console.error('[AgentDashboard] loadOffers error', e);
      this.offers.set([]);
    } finally {
      this.offersLoading.set(false);
    }
  }

  async acceptOffer(o: OfferVM) {
    try {
      await this.callById(
        this.offerApi,
        ['acceptOffer', 'approveOffer', 'accept', 'approve'],
        o.id
      );
      this.loadOffers();
    } catch (e) {
      console.error('[AgentDashboard] acceptOffer error', e);
    }
  }

  async declineOffer(o: OfferVM) {
    try {
      await this.callById(
        this.offerApi,
        ['rejectOffer', 'declineOffer', 'reject', 'decline'],
        o.id
      );
      this.loadOffers();
    } catch (e) {
      console.error('[AgentDashboard] declineOffer error', e);
    }
  }

  // Counter-offer
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
  async sendCounter() {
    const id = this.counterId();
    const amount = this.counterAmount();
    const message = this.counterMessage();
    if (!id || !amount || amount <= 0) return;

    const payloads = [
      { offerId: id, amount, message },
      { id, amount, message },
      { amount, message },
    ];

    for (const p of payloads) {
      try {
        await this.callById(
          this.offerApi,
          ['counterOffer', 'makeCounterOffer', 'proposeCounter', 'counter'],
          id,
          p
        );
        this.cancelCounter();
        this.loadOffers();
        return;
      } catch {
        // prova il prossimo payload
      }
    }
    console.error(
      '[AgentDashboard] sendCounter error: nessun metodo compatibile trovato'
    );
  }
}
