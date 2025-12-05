import { computed, Injectable, signal, WritableSignal } from '@angular/core';
import { PaginatorRequest } from '../../interfaces/paginator-request';

@Injectable({
  providedIn: 'root',
})
export class OffersPaginatorService {
  private _defaultSize = 5;
  private _defaultPage = 1;
  private _offerPaginatorRequest: WritableSignal<PaginatorRequest> = signal({
    size: this._defaultSize,
    page: this._defaultPage,
  });
  private _totalPagesNumber = signal(1);
  private _offerPaginatorRefresher = signal(0);

  size = computed(() => this._offerPaginatorRequest().size);
  page = computed(() => this._offerPaginatorRequest().page);
  offerRequest = computed(() => this._offerPaginatorRequest());
  totalPagesNumber = computed(() => this._totalPagesNumber());
  searchPaginatorTracker = computed(() => this._offerPaginatorRefresher());

  setSize(size: string) {
    this._offerPaginatorRequest.update((offerPaginatorRequest) => ({
      ...offerPaginatorRequest,
      size: Number(size),
    }));
  }

  nextPage() {
    const nextPage = this.page() + 1;
    if (nextPage < this.totalPagesNumber()) {
      this.setPage(Number(nextPage));
    }
  }

  previousPage() {
    const previousPage = this.page() - 1;
    if (previousPage > 0) {
      this.setPage(Number(previousPage));
    }
  }

  setSearchFooterTracker(value: number) {
    this._offerPaginatorRefresher.set(value);
  }

  setPage(page: number) {
    this._offerPaginatorRequest.update((offerPaginatorRequest) => ({
      ...offerPaginatorRequest,
      page: Number(page),
    }));
  }

  setPagesNumber(num: number) {
    this._totalPagesNumber.set(num);
  }

  refresh() {
    this._offerPaginatorRequest.set({
      size: this._defaultSize,
      page: this._defaultPage,
    });
  }
}
