import { Injectable, signal, inject } from '@angular/core';
import { SearchControllerService } from '../../services/services/search-controller.service';
import { SearchRequest } from '../../services/models/search-request';
import { RealEstateResponse } from '../../services/models/real-estate-response';
import { CadastralFilterControllerService } from '../../services/services/cadastral-filter-controller.service';
import { CadastralFilterRequest } from '../../services/models/cadastral-filter-request';

export type Category = 'SALE' | 'RENT';

export type SearchCard = RealEstateResponse & {
  coverUrl?: string;
  title?: string;
  city?: string;
  price?: number | null;
};

export type SearchFormBag = Partial<{
  category: 'SALE' | 'RENT';
  detailId: number | null;
  page: number;
  size: number;
  minPrice: number;
  maxPrice: number;
  minRooms: number;
  maxRooms: number;
  minSquareMeters: number;
  maxSquareMeters: number;
  minFloor: number;
  maxFloor: number;
  minEnergyClass: number;
  maxEnergyClass: number;

  rooms: number;
  sizeMin: number;
  sizeMax: number;
  orderBy: string;
  type: string;

  text: string;
}>;

@Injectable({ providedIn: 'root' })
export class SearchFacade {
  private api = inject(SearchControllerService);

  private filterApi = inject(CadastralFilterControllerService);

  loading = signal(false);
  error = signal<string | null>(null);
  results = signal<SearchCard[]>([]);

  private lastForm = signal<SearchFormBag | null>(null);

  private getUserEmail(): string {
    return localStorage.getItem('userEmail') ?? 'guest@example.com';
  }

  private toCard(r: RealEstateResponse): SearchCard {
    return {
      ...r,
      coverUrl: r.images?.[0],
      title: r.description,
      city: (r as any).city ?? undefined,
      price: (r as any).price ?? null,
    };
  }

  private textMatches(card: SearchCard, text?: string): boolean {
    if (!text) return true;
    const t = text.trim().toLowerCase();
    if (!t) return true;
    const hay = [
      card.title,
      card.description,
      card.city,
      card.category,
      String(card.id ?? ''),
    ]
      .filter(Boolean)
      .join(' ')
      .toLowerCase();
    return hay.includes(t);
  }

  search(v: SearchFormBag): void {
    const category: Category = (v.category ?? 'SALE') as Category;
    const page = v.page ?? 0;
    const size = v.size ?? 12;

    const filterReq: CadastralFilterRequest = {
      minPrice: v.minPrice ?? 0,
      maxPrice: v.maxPrice ?? 1_000_000,
      minRooms: v.minRooms ?? v.rooms ?? 0,
      maxRooms: v.maxRooms ?? (v.rooms && v.rooms > 0 ? v.rooms : 10),
      minSquareMeters: v.sizeMin ?? v.minSquareMeters ?? 0,
      maxSquareMeters: v.sizeMax ?? v.maxSquareMeters ?? 1_000,
      minFloor: v.minFloor ?? 0,
      maxFloor: v.maxFloor ?? 50,
      minEnergyClass: v.minEnergyClass ?? 1,
      maxEnergyClass: v.maxEnergyClass ?? 10,
    };

    this.loading.set(true);
    this.error.set(null);
    this.lastForm.set({ ...v, category, page, size });

    // creo il filtro -> prendo l'id -> faccio la search con quell'id
    this.filterApi.createCadastralFilter({ body: filterReq }).subscribe({
      next: () => {
        const req: SearchRequest = {
          category,
          userEmail: this.getUserEmail(),
          page,
          size,
          detailId: v.detailId ?? undefined,
        };
        this.api.createSearch({ body: req }).subscribe({
          next: (list) => {
            const mapped = (list ?? []).map(this.toCard.bind(this));
            const filtered = mapped.filter((c) => this.textMatches(c, v.text)); // se usi la search bar
            this.results.set(filtered);
            this.loading.set(false);
          },
          error: (err) => {
            this.error.set('Errore durante la ricerca.');
            this.loading.set(false);
          },
        });
      },
      error: (err) => {
        this.error.set('Errore nella creazione del filtro.');
        this.loading.set(false);
      },
    });
  }

  nextPage(): void {
    const lf = this.lastForm();
    if (!lf) return;
    this.search({ ...lf, page: (lf.page ?? 0) + 1 });
  }

  prevPage(): void {
    const lf = this.lastForm();
    if (!lf) return;
    const p = (lf.page ?? 0) - 1;
    this.search({ ...lf, page: p < 0 ? 0 : p });
  }
}
