import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

export type VisitStatus = 'PENDING' | 'APPROVED' | 'DECLINED';
export type OfferStatus = 'PENDING' | 'ACCEPTED' | 'DECLINED' | 'COUNTERED';

export interface Ad {
  id: number;
  title: string;
  city?: string;
  price?: number;
  active?: boolean;
  createdAt?: string;
}
export interface Visit {
  id: number;
  adId: number;
  adTitle: string;
  requesterName: string;
  requesterEmail?: string;
  requestedAt: string;
  preferredDate?: string;
  status: VisitStatus;
}
export interface Offer {
  id: number;
  adId: number;
  adTitle: string;
  bidderName: string;
  amount: number;
  createdAt: string;
  status: OfferStatus;
  message?: string;
}

@Injectable({ providedIn: 'root' })
export class AgentService {
  private http = inject(HttpClient);

  listAds() {
    return this.http.get<Ad[]>('/api/agent/ads');
  }
  createAd(body: any) {
    return this.http.post('/api/agent/ads', body);
  }

  listVisits(status?: VisitStatus) {
    const params = status ? new HttpParams().set('status', status) : undefined;
    return this.http.get<Visit[]>('/api/agent/visits', { params });
  }
  updateVisitStatus(id: number, status: VisitStatus) {
    return this.http.patch(`/api/agent/visits/${id}`, { status });
  }

  listOffers(status?: OfferStatus) {
    const params = status ? new HttpParams().set('status', status) : undefined;
    return this.http.get<Offer[]>('/api/agent/offers', { params });
  }
  respondOffer(id: number, action: 'ACCEPT' | 'DECLINE') {
    return this.http.patch(`/api/agent/offers/${id}`, { action });
  }
  counterOffer(id: number, amount: number, message?: string) {
    return this.http.post(`/api/agent/offers/${id}/counter`, {
      amount,
      message,
    });
  }
}
