import { computed, Injectable, signal, WritableSignal } from '@angular/core';
import { PaginatorRequest } from '../../interfaces/paginator-request';

@Injectable({
  providedIn: 'root',
})
export class AdsPaginatorService {
  private _defaultSize = 5;
  private _defaultPage = 1;
  private _adsPaginatorRequest: WritableSignal<PaginatorRequest> = signal({
    size: this._defaultSize,
    page: this._defaultPage,
  });
  private _totalPagesNumber = signal(1);
  private _adsPaginatorRefresher = signal(0);

  size = computed(() => this._adsPaginatorRequest().size);
  page = computed(() => this._adsPaginatorRequest().page);
  adsRequest = computed(() => this._adsPaginatorRequest());
  totalPagesNumber = computed(() => this._totalPagesNumber());
  adsPaginatorTracker = computed(() => this._adsPaginatorRefresher());

  setSize(size: string) {
    this._adsPaginatorRequest.update((adsPaginatorRequest) => ({
      ...adsPaginatorRequest,
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

  setAdsFooterTracker(value: number) {
    this._adsPaginatorRefresher.set(value);
  }

  setPage(page: number) {
    this._adsPaginatorRequest.update((adsPaginatorRequest) => ({
      ...adsPaginatorRequest,
      page: Number(page),
    }));
  }

  setPagesNumber(num: number) {
    this._totalPagesNumber.set(num);
  }

  refresh() {
    this._adsPaginatorRequest.set({
      size: this._defaultSize,
      page: this._defaultPage,
    });
  }
}
