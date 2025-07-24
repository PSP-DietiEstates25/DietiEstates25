import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Coords {
  lat: number;
  lng: number;
}

@Injectable({ providedIn: 'root' })
export class LocationService {
  private baseUrl = 'http://localhost:8080/api/locations';

  constructor(private http: HttpClient) {}

  /*
   * Cerca le località che contengono la stringa `query`.
   * Esempio di chiamata al backend: GET /api/locations/search?query=Milano
   */
  search(query: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/search?query=${encodeURIComponent(query)}`);
  }

  /*
   * Dato un indirizzo completo (ad es. "Via Roma 10, Milano"),
   * restituisce le coordinate {lat, lng}. 
   * L’endpoint di backend dovrebbe essere:
   * GET /api/locations/coords?address=Via%20Roma%2010%2C%20Milano
   */
  getCoordsFromAddress(address: string): Observable<Coords> {
    const url = `${this.baseUrl}/coords?address=${encodeURIComponent(address)}`;
    return this.http.get<Coords>(url);
  }
}