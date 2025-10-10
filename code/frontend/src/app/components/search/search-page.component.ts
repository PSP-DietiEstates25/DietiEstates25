import { Component, inject, signal } from '@angular/core';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { SearchBarComponent } from '../../shared/components/search-bar/search-bar.component';
import { ReactiveFormsModule } from '@angular/forms';
import { Category, SearchFacade } from './search.facade';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-search-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NavbarComponent,
    SearchBarComponent,
    FormsModule,
    DecimalPipe,
  ],
  templateUrl: './search-page.component.html',
})
export class SearchPageComponent {
  private facade = inject(SearchFacade);

  loading = this.facade.loading;
  error = this.facade.error;
  results = this.facade.results;

  priceMin = 0;
  priceMax = 1_000_000;
  rooms = 0;
  type: Category = 'SALE';
  propertyType: string = '';
  sizeMin = 0;
  sizeMax = 1_000;
  orderBy = 'RECENT';

  page = 1;
  size = 12;
  detailId: number | null = 1;
  searchText: string = '';

  apply(): void {
    console.log('[SearchPage] apply clicked', {
      detailId: this.detailId,
      page: this.page,
    });

    if (this.detailId == null) {
      this.error.set('Seleziona i dettagli (detailId) prima di cercare.');
      return;
    }

    const safePage = Math.max(1, this.page);
    const safeSize = Math.max(1, this.size);

    this.facade.search({
      category: this.type,
      detailId: this.detailId,
      page: safePage,
      size: safeSize,

      minPrice: this.priceMin,
      maxPrice: this.priceMax,
      minRooms: this.rooms,
      maxRooms: this.rooms > 0 ? this.rooms : 10,
      sizeMin: this.sizeMin,
      sizeMax: this.sizeMax,

      orderBy: this.orderBy,
      type: this.propertyType,

      text: this.searchText,
    });
  }

  clear(): void {
    this.priceMin = 0;
    this.priceMax = 1_000_000;
    this.rooms = 0;
    this.type = 'SALE';
    this.sizeMin = 0;
    this.sizeMax = 1_000;
    this.orderBy = 'RECENT';
    this.page = 1;
    this.size = 12;
    this.detailId = null;
    this.searchText = '';
    this.apply();
  }

  onSearch(ev: unknown) {
    const text =
      typeof ev === 'string'
        ? ev
        : (ev as any)?.target?.value ?? (ev as any)?.detail ?? '';

    this.searchText = text;
    this.apply();
  }

  title = signal('Risultati ricerca');
}
