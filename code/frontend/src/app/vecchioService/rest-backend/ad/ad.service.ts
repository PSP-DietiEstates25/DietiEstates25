import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Ad {
  id?: number;
  title: string;
  description: string;
  // altri campi
}

export interface AdDetail {
  id: number;
  title: string;
  price?: number | null;
  city?: string;
  address?: string;
  description?: string;
  type?: string;
  size?: number | null;
  rooms?: number | null;
  floor?: number | null;
  energyClass?: string | null;
  images?: string[];
  coverUrl?: string;
  createdAt?: string;
  lat?: number | null;
  lng?: number | null;
}

@Injectable({
  providedIn: 'root',
})
export class AdService {
  private baseUrl = 'http://localhost:8080/api/ads';

  constructor(private http: HttpClient) {}

  // Ottieni tutti gli annunci
  getAds(): Observable<Ad[]> {
    return this.http.get<Ad[]>(this.baseUrl);
  }

  // Ottieni un annuncio per ID
  getAdById(id: number): Observable<Ad> {
    return this.http.get<Ad>(`${this.baseUrl}/${id}`);
  }

  // Crea un nuovo annuncio
  createAd(ad: Ad): Observable<Ad> {
    return this.http.post<Ad>(this.baseUrl, ad);
  }

  // Aggiorna un annuncio esistente
  updateAd(id: number, ad: Ad): Observable<Ad> {
    return this.http.put<Ad>(`${this.baseUrl}/${id}`, ad);
  }

  // Elimina un annuncio
  deleteAd(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getById(id: number) {
    return this.http.get<AdDetail>(`${this.baseUrl}/${id}`);
  }
  makeOffer(id: number, body: { amount: number }) {
    return this.http.post(`${this.baseUrl}/${id}/offers`, body);
  }
  requestVisit(
    id: number,
    body: { date: string; time?: string }
  ) {
    return this.http.post(`${this.baseUrl}/${id}/visits`, body);
  }
}
