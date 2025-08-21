import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LocationService {
  search(query: string): Observable<string[]> {
    return of([]); 
  }
}
