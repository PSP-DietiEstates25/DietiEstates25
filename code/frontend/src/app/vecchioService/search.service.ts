import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

export type OrderBy = 'recent' | 'price_asc' | 'price_desc';
export interface Filters {
  priceMin: number | null;
  priceMax: number | null;
  rooms: number | null;
  type: string | null;
  sizeMin: number | null;
  sizeMax: number | null;
  orderBy: OrderBy;
}

const DEFAULT: Filters = {
  priceMin: null,
  priceMax: null,
  rooms: null,
  type: null,
  sizeMin: null,
  sizeMax: null,
  orderBy: 'recent',
};

@Injectable({ providedIn: 'root' })
export class SearchService {
  constructor(private router: Router) {}

  private _q = signal('');
  q() {
    return this._q();
  }
  setQuery(v: string) {
    this._q.set(v);
  }

  private _filters = signal<Filters>(DEFAULT);
  filters() {
    return this._filters();
  }
  patchFilters(p: Partial<Filters>) {
    this._filters.update((f) => ({ ...f, ...p }));
  }
  clearFilters() {
    this._filters.set(DEFAULT);
  }

  goToSearch() {
    const f = this._filters();
    const params: any = {};
    if (this._q().trim()) params.q = this._q().trim();
    if (f.priceMin != null) params.priceMin = f.priceMin;
    if (f.priceMax != null) params.priceMax = f.priceMax;
    if (f.rooms != null) params.rooms = f.rooms;
    if (f.type) params.type = f.type;
    if (f.sizeMin != null) params.sizeMin = f.sizeMin;
    if (f.sizeMax != null) params.sizeMax = f.sizeMax;
    if (f.orderBy) params.orderBy = f.orderBy;
    this.router.navigate(['/search'], { queryParams: params });
  }
}
