import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LocationService {
  private baseUrl = 'http://localhost:8080/api/locations';

  constructor(private http: HttpClient) {}

  search(query: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/search?query=${query}`);
  }
}