import { Injectable, inject, signal, computed } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError, finalize, map, switchMap, tap } from 'rxjs/operators';
import { LocalStorageService } from '../../manual_services/local-storage.service';

import { NotificationControllerService } from '../../services/services/notification-controller.service';
import { NotificationCategoryControllerService } from '../../services/services/notification-category-controller.service';

import { PageNotificationResponse } from '../../services/models/page-notification-response';
import { NotificationResponse } from '../../services/models/notification-response';
import { NotificationCategoryResponse } from '../../services/models/notification-category-response';
import { UpdateNotificationCategoryStatusRequest } from '../../services/models/update-notification-category-status-request';

import {
  NotificationCategory,
  NotificationPreferenceVM,
  adaptUserPreferences,
} from './notification-preferences.adapter';

export interface NotificationItemVM {
  id: number;
  message: string;
  createdDate: string;
  category: NotificationCategory;
  realEstateId?: number;
  realEstateLabel?: string;
}


@Injectable({ providedIn: 'root' })
export class NotificationsFacade {
  private readonly notificationService = inject(NotificationControllerService);
  private readonly categoryService = inject(
    NotificationCategoryControllerService
  );

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  private readonly _preferences = signal<NotificationPreferenceVM[]>([]);
  private readonly _notifications = signal<NotificationItemVM[]>([]);

  readonly unreadCount = computed(() => {
    const lastSeenIso = this._lastSeen();
    if (!lastSeenIso) return this._notifications().length;

    const last = new Date(lastSeenIso).getTime();
    return this._notifications().filter((n) => {
      const t = new Date(n.createdDate).getTime();
      return !Number.isNaN(t) && t > last;
    }).length;
  });

  readonly filterCategories = computed(() => this._filterCategories());

  private readonly _query = signal('');
  private readonly _filterCategories = signal<NotificationCategory[]>([]);

  private readonly _page = signal(0);
  private readonly _hasMore = signal(true);
  private readonly PAGE_SIZE = 12;

  readonly userPreferences = computed(() => this._preferences());

  readonly filtered = computed(() => {
    const query = this._query().trim().toLowerCase();
    const cats = this._filterCategories();

    return this._notifications().filter((n) => {
      if (cats.length && !cats.includes(n.category)) return false;
      if (query && !n.message.toLowerCase().includes(query)) return false;
      return true;
    });
  });

  private readonly localStorage = inject(LocalStorageService);
  private readonly LAST_SEEN_KEY = 'notifications_last_seen';
  private readonly _lastSeen = signal<string | null>(
    this.localStorage.getItem(this.LAST_SEEN_KEY)
  );


  init(): void {
    this.loading.set(true);
    this.error.set(null);
    this._notifications.set([]);
    this._page.set(0);
    this._hasMore.set(true);

    this.categoryService
      .getUserNotificationCategories({})
      .pipe(
        map((res) => adaptUserPreferences(res)),
        tap((prefs) => this._preferences.set(prefs)),
        switchMap((prefs) => {
          const enabled = prefs.filter((p) => p.enabled).map((p) => p.category);
          if (!enabled.length) {
            this._hasMore.set(false);
            return of<void>(undefined);
          }
          return this.loadPageInternal(enabled, 0);
        }),
        catchError((err) => {
          console.error('[NotificationsFacade] init error', err);
          this.error.set('Errore nel caricamento delle notifiche.');
          this._preferences.set([]);
          this._notifications.set([]);
          this._hasMore.set(false);
          return of<void>(undefined);
        }),
        finalize(() => this.loading.set(false))
      )
      .subscribe();
  }

  markAllSeen(): void {
    const now = new Date().toISOString();
    this._lastSeen.set(now);
    this.localStorage.setItem(this.LAST_SEEN_KEY, now);
  }

  loadMore(): void {
    if (this.loading() || !this._hasMore()) return;

    const enabled = this._preferences()
      .filter((p) => p.enabled)
      .map((p) => p.category);

    if (!enabled.length) return;

    this.loading.set(true);
    const nextPage = this._page() + 1;

    this.loadPageInternal(enabled, nextPage)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe();
  }

  setCategoryEnabled(category: NotificationCategory, enabled: boolean): void {
    const before = this._preferences();

    this._preferences.set(
      before.map((p) => (p.category === category ? { ...p, enabled } : p))
    );

    const body: UpdateNotificationCategoryStatusRequest = {
      name: category,
      isActive: enabled,
    };

    this.categoryService
      .updateIsActive({
        notificationcategoryname: category,
        body,
      })
      .pipe(
        catchError((err) => {
          console.error('[NotificationsFacade] setCategoryEnabled error', err);
          this.error.set("Errore nell'aggiornare la preferenza.");
          this._preferences.set(before);
          return of(null);
        })
      )
      .subscribe();
  }

  toggleFilterCat(category: NotificationCategory): void {
    const current = this._filterCategories();
    const exists = current.includes(category);
    this._filterCategories.set(
      exists ? current.filter((c) => c !== category) : [...current, category]
    );
  }

  setQuery(query: string): void {
    this._query.set(query ?? '');
  }

  clearFilters(): void {
    this._query.set('');
    this._filterCategories.set([]);
  }

  private loadPageInternal(categories: NotificationCategory[], page: number) {
    if (!categories.length) {
      return of<void>(undefined);
    }

    const size = this.PAGE_SIZE;

    const calls = categories.map((cat) =>
      this.notificationService
        .getNotificationCategoryNotifications({
          notificationcategoryname: cat,
          page,
          size,
        })
        .pipe(
          catchError((err) => {
            console.error(
              '[NotificationsFacade] error loading notifications for category',
              cat,
              err
            );
            const empty: PageNotificationResponse = {
              content: [],
              first: page === 0,
              last: true,
              number: page,
              numberOfElements: 0,
              size,
              totalElements: 0,
              totalPages: 0,
            };
            return of(empty);
          })
        )
    );

    return forkJoin(calls).pipe(
      tap((responses) => {
        const existing = this._notifications();
        const byId = new Map<number, NotificationItemVM>();
        for (const n of existing) {
          byId.set(n.id, n);
        }

        responses.forEach((res, idx) => {
          const cat = categories[idx];
          (res.content ?? []).forEach((n: NotificationResponse) => {
            if (n.id == null) {
              return;
            }
            byId.set(n.id, this.toNotificationItemVM(n, cat));
          });
        });

        const merged = Array.from(byId.values()).sort(
          (a, b) =>
            new Date(b.createdDate).getTime() -
            new Date(a.createdDate).getTime()
        );

        this._notifications.set(merged);

        const anyHasMore = responses.some((r) => r && r.last === false);
        this._hasMore.set(anyHasMore);
        if (anyHasMore) {
          this._page.set(page);
        }
      }),
      map(() => void 0)
    );
  }

  private toNotificationItemVM(
    res: NotificationResponse,
    category: NotificationCategory
  ): NotificationItemVM {
    const created =
      res.createdDate ?? res.lastModifiedDate ?? new Date().toISOString();

    const id = res.id ?? Math.floor(Math.random() * 1_000_000_000);

    return {
      id,
      message: res.message ?? '',
      createdDate: created,
      category,
    };
  }
}
