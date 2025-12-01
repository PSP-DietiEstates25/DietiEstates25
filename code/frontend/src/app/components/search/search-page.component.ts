import { Component, inject, signal, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpBackend, HttpClient } from '@angular/common/http';

import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
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
    NavbarComponent,
    FilterPanelComponent,
    RecentSearchesComponent,
  ],
  templateUrl: './search-page.component.html',
})
export class SearchPageComponent implements OnDestroy {
  facade = inject(SearchFacade);

  private httpBackend = inject(HttpBackend);
  private httpNoInter = new HttpClient(this.httpBackend);

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

  goNextPage() {
    const res = this.facade.nextPage();
    if (res && typeof (res as any).subscribe === 'function') {
      (res as any).subscribe({
        error: () => {
          // gestire errori
        },
      });
    }
  }

  goPrevPage() {
    const res = this.facade.prevPage();
    if (res && typeof (res as any).subscribe === 'function') {
      (res as any).subscribe({
        error: () => {
          // idem
        },
      });
    }
  }

  ngOnDestroy(): void {
    for (const url of this.blobCache.values()) URL.revokeObjectURL(url);
    this.blobCache.clear();
    this.pending.clear();
  }

  ngOnInit() {
    /*
    this.facade
      .runFullSearch({
        category: 'SALE',
        page: 1,
        size: 12,
        userEmail: '',
        geographicalPosition: {
          address: '',
          city: '',
          latitude: 0,
          longitude: 0,
          municipality: '',
        },
        utility: {
          hasAirConditioning: false,
          hasDoorman: false,
          hasElevator: false,
          nearPark: false,
          nearPublicTransport: false,
          nearSchool: false,
        },
        cadastralFilter: {
          minPrice: 0,
          maxPrice: 999999999,
          minSquareMeters: 0,
          maxSquareMeters: 100000,
          minRooms: 0,
          maxRooms: 50,
          minFloor: -10,
          maxFloor: 100,
          minEnergyClass: 0,
          maxEnergyClass: 9,
        },
      })
      .subscribe();
      */
  }
}
