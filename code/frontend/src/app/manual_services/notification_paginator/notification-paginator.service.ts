import { computed, Injectable, signal, WritableSignal } from '@angular/core';
import { NotificationPaginatorRequest } from '../../interfaces/notification-paginator-request';

@Injectable({
  providedIn: 'root',
})
export class NotificationPaginatorService {
  private _defaultSize = 5;
  private _defaultPage = 1;
  private _defaultCategories = null;
  private _notificationPaginatorRequest: WritableSignal<NotificationPaginatorRequest> =
    signal({
      size: this._defaultSize,
      page: this._defaultPage,
      categories: this._defaultCategories,
    });
  private _totalPagesNumber = signal(1);
  private _notificationPaginatorRefresher = signal(0);

  size = computed(() => this._notificationPaginatorRequest().size);
  page = computed(() => this._notificationPaginatorRequest().page);
  notificationRequest = computed(() => this._notificationPaginatorRequest());
  totalPagesNumber = computed(() => this._totalPagesNumber());
  notificationPaginatorTracker = computed(() =>
    this._notificationPaginatorRefresher(),
  );

  setSize(size: string) {
    this._notificationPaginatorRequest.update(
      (notificationPaginatorRequest) => ({
        ...notificationPaginatorRequest,
        size: Number(size),
      }),
    );
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

  setNotificationFooterTracker(value: number) {
    this._notificationPaginatorRefresher.set(value);
  }

  setPage(page: number) {
    this._notificationPaginatorRequest.update(
      (notificationPaginatorRequest) => ({
        ...notificationPaginatorRequest,
        page: Number(page),
      }),
    );
  }

  setPagesNumber(num: number) {
    this._totalPagesNumber.set(num);
  }

  setCategory(
    category: 'NEW_PROPERTIES' | 'PROMOTIONAL' | 'VISIT' | 'OFFER' | null,
  ) {
    this._notificationPaginatorRequest.update((req) => ({
      ...req,
      category: category,
      page: 1,
    }));
  }

  refresh() {
    this._notificationPaginatorRequest.set({
      size: this._defaultSize,
      page: this._defaultPage,
      categories: this._defaultCategories,
    });
  }
}
