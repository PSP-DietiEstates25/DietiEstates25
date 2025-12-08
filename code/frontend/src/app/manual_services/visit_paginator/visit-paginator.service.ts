import { computed, Injectable, signal, WritableSignal } from '@angular/core';
import { OfferPaginatorRequest } from '../../interfaces/offer-paginator-request';
import { VisitPaginatorRequest } from '../../interfaces/visit-paginator-request';

@Injectable({
  providedIn: 'root',
})
export class VisitPaginatorService {
  private _defaultSize = 5;
  private _defaultPage = 1;
  private _defaultStatus = null;
  private _visitPaginatorRequest: WritableSignal<VisitPaginatorRequest> =
    signal({
      size: this._defaultSize,
      page: this._defaultPage,
      status: this._defaultStatus,
    });
  private _totalPagesNumber = signal(1);
  private _visitPaginatorRefresher = signal(0);

  size = computed(() => this._visitPaginatorRequest().size);
  page = computed(() => this._visitPaginatorRequest().page);
  visitRequest = computed(() => this._visitPaginatorRequest());
  totalPagesNumber = computed(() => this._totalPagesNumber());
  visitPaginatorTracker = computed(() => this._visitPaginatorRefresher());

  setSize(size: string) {
    this._visitPaginatorRequest.update((visitPaginatorRequest) => ({
      ...visitPaginatorRequest,
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
    this._visitPaginatorRefresher.set(value);
  }

  setPage(page: number) {
    this._visitPaginatorRequest.update((visitPaginatorRequest) => ({
      ...visitPaginatorRequest,
      page: Number(page),
    }));
  }

  setPagesNumber(num: number) {
    this._totalPagesNumber.set(num);
  }

  setStatus(status: 'PENDING' | 'ACCEPTED' | 'REJECTED' | null) {
    this._visitPaginatorRequest.update((req) => ({
      ...req,
      status: status,
      page: 1,
    }));
  }

  refresh() {
    this._visitPaginatorRequest.set({
      size: this._defaultSize,
      page: this._defaultPage,
      status: this._defaultStatus,
    });
  }
}
