import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ServiceDTO } from '../../interfaces/service-dto';

@Injectable({ providedIn: 'root' })
export class MetadataService {
  private baseUrl = 'http://localhost:8080/api/metadata';

  constructor(private http: HttpClient) {}

  /**
   * Restituisce un array di stringhe corrispondenti alle categorie
   * Esempio di risposta: ["RENT", "SALE"]
   */
  getCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/categories`);
  }

  /**
   * Restituisce un array di ServiceDto { id, name }
   * Esempio di risposta: [{ id: 1, name: "Balcony" }, { id: 2, name: "Parking" }, ...]
   */
  getServices(): Observable<ServiceDTO[]> {
    return this.http.get<ServiceDTO[]>(`${this.baseUrl}/services`);
  }
}