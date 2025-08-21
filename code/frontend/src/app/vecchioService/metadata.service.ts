import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ServiceDTO } from '../interfaces/service-dto';

@Injectable({ providedIn: 'root' })
export class MetadataService {
  getCategories(): Observable<string[]> {
    // prendi dal backend
    return of(['Apartment', 'House', 'Studio']);
  }
  getServices(): Observable<ServiceDTO[]> {
    // prendi dal backend
    return of([]);
  }
}
