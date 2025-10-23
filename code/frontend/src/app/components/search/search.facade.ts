import { Injectable, signal, inject } from '@angular/core';
import { SearchControllerService } from '../../services/resource_server/services';
import { SearchRequest } from '../../services/resource_server/models';
import { RealEstateResponse } from '../../services/resource_server/models';
import { CadastralFilterControllerService } from '../../services/resource_server/services';
import { CadastralFilterRequest } from '../../services/resource_server/models';

import { of, switchMap } from 'rxjs';
import { DetailControllerService } from '../../services/resource_server/services';

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

  cadastralFilterId: number | null;
}>;

@Injectable({ providedIn: 'root' })
export class SearchFacade {
  private api = inject(SearchControllerService);

  private filterApi = inject(CadastralFilterControllerService);

  private detailsApi = inject(DetailControllerService);

  loading = signal(false);
  error = signal<string | null>(null);
  results = signal<SearchCard[]>([]);

  private cachedDetailId: number | null =
    Number(localStorage.getItem('detailId')) || null;

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

  private ensureDetailId(v: SearchFormBag) {
    if (v.detailId != null) return of(v.detailId);
    if (this.cachedDetailId != null) return of(this.cachedDetailId);

    const body: any = {};

    return this.detailsApi.createDetail$Response({ body }).pipe(
      switchMap((res: any) => {
        const b: any = res.body;
        let id: number | undefined =
          b && typeof b.id === 'number' ? b.id : undefined;
        if (!id) {
          const loc =
            res.headers.get('Location') ?? res.headers.get('location');
          const last = loc?.split('/').filter(Boolean).pop();
          const parsed = last ? Number(last) : NaN;
          id = Number.isFinite(parsed) ? parsed : undefined;
        }
        if (!id) throw new Error('Impossibile ottenere detailId');
        this.cachedDetailId = id;
        localStorage.setItem('detailId', String(id));
        return of(id);
      })
    );
  }

  private parseIdFromLocation(loc?: string | null): number | undefined {
    if (!loc) return undefined;
    const last = loc.split('/').filter(Boolean).pop();
    const id = last ? Number(last) : NaN;
    return Number.isFinite(id) ? id : undefined;
  }

  search(v: SearchFormBag): void {
    const category: Category = (v.category ?? 'SALE') as Category;
    const page = Math.max(1, v.page ?? 1);
    const size = Math.max(1, v.size ?? 12);

    const pMin = Math.max(1, v.minPrice ?? 1);
    const pMax = Math.max(pMin, v.maxPrice ?? 1_000_000);

    const rMinUser = v.minRooms ?? v.rooms; // se usi "rooms" singolo
    const rMin = Math.max(1, rMinUser ?? 1);
    const rMax = Math.max(
      rMin,
      v.maxRooms ?? (rMinUser && rMinUser > 0 ? rMinUser : 10)
    );

    const smMin = Math.max(1, v.sizeMin ?? v.minSquareMeters ?? 1);
    const smMax = Math.max(smMin, v.sizeMax ?? v.maxSquareMeters ?? 1_000);

    const fMin = Math.max(1, v.minFloor ?? 1);
    const fMax = Math.max(fMin, v.maxFloor ?? 50);

    const eMin = Math.max(1, v.minEnergyClass ?? 1);
    const eMax = Math.max(eMin, v.maxEnergyClass ?? 10);

    const filterReq: CadastralFilterRequest = {
      minPrice: pMin,
      maxPrice: pMax,
      minRooms: rMin,
      maxRooms: rMax,
      minSquareMeters: smMin,
      maxSquareMeters: smMax,
      minFloor: fMin,
      maxFloor: fMax,
      minEnergyClass: eMin,
      maxEnergyClass: eMax,
    };

    this.loading.set(true);
    this.error.set(null);
    this.lastForm.set({ ...v, category, page, size });

    this.ensureDetailId(v)
      .pipe(
        switchMap((detailId: number) =>
          this.filterApi
            .createCadastralFilter$Response({ body: filterReq })
            .pipe(
              switchMap((res: any) => {
                const body: any = res.body as any;
                let cadastralFilterId: number | undefined =
                  body && typeof body.id === 'number' ? body.id : undefined;
                if (!cadastralFilterId) {
                  const loc =
                    res.headers.get('Location') ?? res.headers.get('location');
                  cadastralFilterId = this.parseIdFromLocation(loc);
                }

                const req: SearchRequest = {
                  category,
                  userEmail: this.getUserEmail(),
                  page,
                  size,
                  cadastralFilterId,
                  detailId,
                };
                return this.api.createSearch({ body: req });
              })
            )
        )
      )
      .subscribe({
        next: (list) => {
          const mapped = (list ?? []).map(this.toCard.bind(this));
          const filtered = mapped.filter((c) => this.textMatches(c, v.text));
          this.results.set(filtered);
          this.loading.set(false);
        },
        error: (err) => {
          console.error(err);
          this.error.set(err?.message ?? 'Errore durante la ricerca.');
          this.loading.set(false);
        },
      });
  }

  nextPage(): void {
    const lf = this.lastForm();
    if (!lf) return;
    const next = Math.max(1, (lf.page ?? 1) + 1);
    this.search({ ...lf, page: next });
  }

  prevPage(): void {
    const lf = this.lastForm();
    if (!lf) return;
    const prev = Math.max(1, (lf.page ?? 1) - 1);
    this.search({ ...lf, page: prev });
  }
}
