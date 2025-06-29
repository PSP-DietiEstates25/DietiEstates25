import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Ad {
  id?: number;
  title: string;
  description: string;
  // altri campi
}

@Injectable({
  providedIn: 'root'
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
}
