// search-page.component.ts
import { Component, inject, signal, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpBackend, HttpClient } from '@angular/common/http';

import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { SearchBarComponent } from '../../shared/components/search-bar/search-bar.component';
import { ResultsMapComponent } from '../resultsMap/results-map.component';
import { FilterPanelComponent } from '../../shared/components/filter-panel/filter-panel.component';
import { RecentSearchesComponent } from '../recent-searches/recent-searches.component';

import { SearchFacade } from './search.facade';

const isHttp = (s: string) => /^https?:\/\//i.test(s);
const isData = (s: string) => /^data:/i.test(s);
const looksJpeg = (b64: string) => b64?.startsWith('/9j/');
const looksPng = (b64: string) => b64?.startsWith('iVBOR');

@Component({
  selector: 'app-search-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    NavbarComponent,
    SearchBarComponent,
    ResultsMapComponent,
    FilterPanelComponent,
    RecentSearchesComponent,
  ],
  templateUrl: './search-page.component.html',
})
export class SearchPageComponent implements OnDestroy {
  facade = inject(SearchFacade);

  private handler = inject(HttpBackend);
  private httpNoInter = new HttpClient(this.handler);

  private blobCache = new Map<string, string>();
  private pending = new Set<string>();

  readonly placeholder = '/assets/placeholder.jpg';

  imgSrc(raw?: string | null): string | null {
    if (!raw) return null;

    if (isHttp(raw)) {
      const cached = this.blobCache.get(raw);
      if (cached) return cached;

      if (!this.pending.has(raw)) {
        this.pending.add(raw);
        this.httpNoInter
          .get(raw, { responseType: 'blob', withCredentials: false })
          .subscribe({
            next: (blob) => {
              const obj = URL.createObjectURL(blob);
              this.blobCache.set(raw, obj);
              this.pending.delete(raw);
            },
            error: () => {
              this.pending.delete(raw);
            },
          });
      }
      return this.placeholder;
    }

    if (isData(raw)) return raw;

    if (raw.startsWith('?') || raw.length < 20) return null;
    const mime = looksJpeg(raw)
      ? 'image/jpeg'
      : looksPng(raw)
      ? 'image/png'
      : 'image/*';
    return `data:${mime};base64,${raw}`;
  }

  ngOnDestroy(): void {
    for (const url of this.blobCache.values()) URL.revokeObjectURL(url);
    this.blobCache.clear();
    this.pending.clear();
  }

  ngOnInit() {
    this.facade
      .runFullSearch({
        category: 'SALE',
        page: 1,
        size: 12,
        userEmail: '',
        geo: {
          address: '',
          city: '',
          latitude: 0,
          longitude: 0,
          municipality: '',
        },
        uti: {
          hasAirConditioning: false,
          hasDoorman: false,
          hasElevator: false,

          nearPark: false,
          nearPublicTransport: false,
          nearSchool: false,
        },
        cf: {
          maxPrice: 0,
          minPrice: 0,
          minRooms: 0,
          maxRooms: 0,
          maxEnergyClass: 0,
          minEnergyClass: 0,
          maxSquareMeters: 0,
          minSquareMeters: 0,
          maxFloor: 0,
          minFloor: 0,
        },
      })
      .subscribe();
  }
}
