import { computed, Injectable, signal, WritableSignal } from '@angular/core';
import { SearchRequest } from '../../services/models';

@Injectable({
  providedIn: 'root',
})
export class SearchPaginatorService {
  private _defaultSize = 5;
  private _defaultPage = 1;
  private _defaultSearchCategory = 'SALE';
  private _searchRequest: WritableSignal<SearchRequest> = signal<SearchRequest>(
    {
      size: this._defaultSize,
      page: this._defaultPage,
      category: this._defaultSearchCategory,
    },
  );
  private _totalPagesNumber = signal(1);
  private _searchPaginatorRefresher = signal(0);

  size = computed(() => this._searchRequest().size);
  page = computed(() => this._searchRequest().page);
  searchCategory = computed(() => this._defaultSearchCategory);
  searchRequest = computed(() => this._searchRequest());
  totalPagesNumber = computed(() => this._totalPagesNumber());
  searchPaginatorTracker = computed(() => this._searchPaginatorRefresher());

  setSize(size: string) {
    this._searchRequest.update((searchRequest) => ({
      ...searchRequest,
      size: Number(size),
    }));
  }

  nextPage() {
    const nextPage = this.page() + 1;
    if (nextPage < this.totalPagesNumber()) {
      this.setPage(String(nextPage));
    }
  }

  previousPage() {
    const previousPage = this.page() - 1;
    if (previousPage > 0) {
      this.setPage(String(previousPage));
    }
  }

  setSearchFooterTracker(value: number) {
    this._searchPaginatorRefresher.set(value);
  }

  setPage(page: string) {
    this._searchRequest.update((searchRequest) => ({
      ...searchRequest,
      page: Number(page),
    }));
  }

  setPagesNumber(num: number) {
    this._totalPagesNumber.set(num);
  }

  refresh() {
    this._searchRequest.set({
      size: this._defaultSize,
      page: this._defaultPage,
      category: this._defaultSearchCategory,
    });
  }
}
