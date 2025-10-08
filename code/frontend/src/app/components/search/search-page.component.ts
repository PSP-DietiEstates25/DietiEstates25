import { Component, inject, signal } from '@angular/core';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { SearchBarComponent } from '../../shared/components/search-bar/search-bar.component';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
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
  priceMax = 1000000;
  rooms = 0;
  type: Category = 'SALE';
  sizeMin = 0;
  sizeMax = 1000;
  orderBy = 'RECENT';

  page = 0;
  size = 12;
  detailId: number | null = null;

  apply(): void {
    this.facade.search({
      category: this.type,
      detailId: this.detailId,
      page: this.page,
      size: this.size,

      minPrice: this.priceMin,
      maxPrice: this.priceMax,
      minRooms: this.rooms,
      maxRooms: this.rooms > 0 ? this.rooms : 10,
      sizeMin: this.sizeMin,
      sizeMax: this.sizeMax,

      orderBy: this.orderBy,
      type: this.type,
    });
  }

  clear(): void {
    this.priceMin = 0;
    this.priceMax = 1000000;
    this.rooms = 0;
    this.type = 'SALE';
    this.sizeMin = 0;
    this.sizeMax = 1000;
    this.orderBy = 'RECENT';
    this.page = 0;
    this.size = 12;
    this.detailId = null;

    this.apply();
  }

  title = signal('Risultati ricerca');
}
