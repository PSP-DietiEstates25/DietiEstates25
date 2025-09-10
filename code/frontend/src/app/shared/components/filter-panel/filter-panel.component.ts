import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SearchService } from '../../services/search.service';

@Component({
  selector: 'app-filter-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './filter-panel.component.html',
  styleUrl: './filter-panel.component.scss',
})
export class FilterPanelComponent {
  private search = inject(SearchService);

  types = ['Appartamento', 'Casa indipendente', 'Stanza', 'Attico'];
  orders = [
    { value: 'recent', label: 'Più recenti' },
    { value: 'price_asc', label: 'Prezzo ↑' },
    { value: 'price_desc', label: 'Prezzo ↓' },
  ] as const;

  f = computed(() => this.search.filters());

  get priceMin() {
    return this.f().priceMin ?? undefined;
  }
  set priceMin(v: number | undefined) {
    this.search.patchFilters({ priceMin: v ?? null });
  }
  get priceMax() {
    return this.f().priceMax ?? undefined;
  }
  set priceMax(v: number | undefined) {
    this.search.patchFilters({ priceMax: v ?? null });
  }
  get rooms() {
    return this.f().rooms ?? undefined;
  }
  set rooms(v: number | undefined) {
    this.search.patchFilters({ rooms: v ?? null });
  }
  get type() {
    return this.f().type ?? '';
  }
  set type(v: string) {
    this.search.patchFilters({ type: v || null });
  }
  get sizeMin() {
    return this.f().sizeMin ?? undefined;
  }
  set sizeMin(v: number | undefined) {
    this.search.patchFilters({ sizeMin: v ?? null });
  }
  get sizeMax() {
    return this.f().sizeMax ?? undefined;
  }
  set sizeMax(v: number | undefined) {
    this.search.patchFilters({ sizeMax: v ?? null });
  }
  get orderBy() {
    return this.f().orderBy;
  }
  set orderBy(v: any) {
    this.search.patchFilters({ orderBy: v });
  }

  apply() {
    this.search.goToSearch();
  }
  clear() {
    this.search.clearFilters();
    this.search.goToSearch();
  }
}
