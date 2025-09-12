import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { SearchBarComponent } from "../../shared/components/search-bar/search-bar.component";

interface Ad {
  id: number;
  title: string;
  city?: string;
  price?: number;
  coverUrl?: string;
}

type OrderBy = 'recent' | 'price_asc' | 'price_desc';
interface Filters {
  priceMin: number | null;
  priceMax: number | null;
  rooms: number | null;
  type: string | null;
  sizeMin: number | null;
  sizeMax: number | null;
  orderBy: OrderBy;
}

function num(v: string | null): number | null {
  const n = v == null ? NaN : Number(v);
  return Number.isFinite(n) ? n : null;
}
function bool(v: string | null): boolean {
  return v === '1' || v === 'true';
}

@Component({
  selector: 'app-search-page',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent, SearchBarComponent],
  templateUrl: './search-page.component.html',
})
export class SearchPageComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private http = inject(HttpClient);

  q = signal('');
  filters = signal<Filters>({
    priceMin: null,
    priceMax: null,
    rooms: null,
    type: null,
    sizeMin: null,
    sizeMax: null,
    orderBy: 'recent',
  });

  results = signal<Ad[]>([]);
  loading = signal(false);
  title = computed(() =>
    this.q() ? `Risultati per "${this.q()}"` : 'Tutti gli annunci'
  );

  get priceMin() {
    return this.filters().priceMin ?? undefined;
  }
  set priceMin(v: number | undefined) {
    this.filters.update((f) => ({ ...f, priceMin: v ?? null }));
  }
  get priceMax() {
    return this.filters().priceMax ?? undefined;
  }
  set priceMax(v: number | undefined) {
    this.filters.update((f) => ({ ...f, priceMax: v ?? null }));
  }
  get rooms() {
    return this.filters().rooms ?? undefined;
  }
  set rooms(v: number | undefined) {
    this.filters.update((f) => ({ ...f, rooms: v ?? null }));
  }
  get type() {
    return this.filters().type ?? '';
  }
  set type(v: string) {
    this.filters.update((f) => ({ ...f, type: v || null }));
  }
  get sizeMin() {
    return this.filters().sizeMin ?? undefined;
  }
  set sizeMin(v: number | undefined) {
    this.filters.update((f) => ({ ...f, sizeMin: v ?? null }));
  }
  get sizeMax() {
    return this.filters().sizeMax ?? undefined;
  }
  set sizeMax(v: number | undefined) {
    this.filters.update((f) => ({ ...f, sizeMax: v ?? null }));
  }
  get orderBy() {
    return this.filters().orderBy;
  }
  set orderBy(v: any) {
    this.filters.update((f) => ({ ...f, orderBy: v }));
  }

  constructor() {
    this.route.queryParamMap.subscribe((pm) => {
      this.fromParams(pm);
      this.fetch();
    });
  }

  private fromParams(pm: ParamMap) {
    this.q.set(pm.get('q') ?? '');
    this.filters.set({
      priceMin: num(pm.get('min')),
      priceMax: num(pm.get('max')),
      rooms: num(pm.get('rooms')),
      type: pm.get('type'),
      sizeMin: num(pm.get('smin')),
      sizeMax: num(pm.get('smax')),
      orderBy: (pm.get('order') as OrderBy) || 'recent',
    });
  }

  private toParams(): Record<string, string> {
    const p: Record<string, string> = {};
    const q = this.q().trim();
    if (q) p['q'] = q;
    const f = this.filters();
    const put = (k: string, v: unknown) => {
      if (v !== null && v !== undefined && v !== '' && v !== false)
        p[k] = String(v);
    };
    put('min', f.priceMin);
    put('max', f.priceMax);
    put('rooms', f.rooms);
    put('type', f.type);
    put('smin', f.sizeMin);
    put('smax', f.sizeMax);
    if (f.orderBy !== 'recent') p['order'] = f.orderBy;
    return p;
  }

  apply() {
    this.router.navigate(['/search'], { queryParams: this.toParams() });
  }
  clear() {
    this.filters.set({
      priceMin: null,
      priceMax: null,
      rooms: null,
      type: null,
      sizeMin: null,
      sizeMax: null,
      orderBy: 'recent',
    });
    this.apply();
  }

  fetch() {
    this.loading.set(true);
    const params = new HttpParams({ fromObject: this.toParams() });
    this.http.get<Ad[]>('/api/ads/search', { params }).subscribe({
      next: (data) => this.results.set(data ?? []),
      error: (_) => this.results.set([]),
      complete: () => this.loading.set(false),
    });
  }
}
