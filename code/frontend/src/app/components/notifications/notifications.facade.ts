import { Injectable, inject, signal, computed } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { LocalStorageService } from '../../manual_services/local-storage/local-storage.service';
import { NotificationControllerService } from '../../services/services/notification-controller.service';
import { NotificationResponse } from '../../services/models/notification-response';
import { NotificationCategory } from '../../enums/notification-category.enum';
import { NotificationPaginatorRequest } from '../../interfaces/notification-paginator-request';

export const ALL_NOTIFICATION_CATEGORIES: NotificationCategory[] = [
  'NEW_PROPERTIES',
  'PROMOTIONAL',
  'VISIT',
  'OFFER',
];

@Injectable({ providedIn: 'root' })
export class NotificationsFacade {
  notificationService = inject(NotificationControllerService);
  localStorage = inject(LocalStorageService);

  readonly notificationsLoading = signal(false);
  readonly error = signal<string | null>(null);

  private readonly _rawNotifications = signal<NotificationResponse[]>([]);

  readonly selectedCategories = signal<NotificationCategory[]>([]);
  readonly searchQuery = signal<string>('');

  readonly allCategories = ALL_NOTIFICATION_CATEGORIES;

  readonly notifications = computed(() => {
    const all = this._rawNotifications();
    const query = this.searchQuery().toLowerCase().trim();

    let filtered = all;

    if (query) {
      filtered = filtered.filter((n) =>
        (n.message || '').toLowerCase().includes(query),
      );
    }

    return filtered;
  });

  private readonly LAST_SEEN_KEY = 'notifications_last_seen';
  private readonly _lastSeen = signal<string | null>(
    this.localStorage.getItem(this.LAST_SEEN_KEY),
  );
  readonly lastSeen = this._lastSeen.asReadonly();

  readonly unreadCount = computed(() => {
    const lastSeenIso = this._lastSeen();
    const currentNotifications = this._rawNotifications();
    if (!lastSeenIso) return currentNotifications.length;
    const last = new Date(lastSeenIso).getTime();
    return currentNotifications.filter((n) => {
      const d = n.createdDate || n.lastModifiedDate;
      const t = d ? new Date(d).getTime() : 0;
      return !Number.isNaN(t) && t > last;
    }).length;
  });

  // --- METODI --
  markAllSeen(): void {
    const now = new Date().toISOString();
    this._lastSeen.set(now);
    this.localStorage.setItem(this.LAST_SEEN_KEY, now);
  }

  toggleCategory(category: NotificationCategory): void {
    if (category === null) return;
    const current = this.selectedCategories();
    if (current.includes(category)) {
      this.selectedCategories.set(current.filter((c) => c !== category));
    } else {
      this.selectedCategories.set([...current, category]);
    }
  }

  setQuery(query: string): void {
    this.searchQuery.set(query);
  }

  clearFilters(): void {
    this.selectedCategories.set([]);
    this.searchQuery.set('');
  }

  isCategorySelected(category: NotificationCategory): boolean {
    return this.selectedCategories().includes(category);
  }

  getNotifications(request: NotificationPaginatorRequest) {
    const params: any = {
      size: request.size,
      page: request.page - 1,
    };

    if (request.categories && request.categories.length > 0) {
      const validCats = request.categories.filter(
        (category) => category !== null,
      );
      if (validCats.length > 0) {
        params.categories = validCats;
      }
    }

    return this.notificationService.getUserNotifications(params);
  }

  fetchNotifications(request: NotificationPaginatorRequest) {
    this.notificationsLoading.set(true);

    return this.getNotifications(request).pipe(
      switchMap((response) => of(response)),
      tap((response) => {
        const content = (response.content || []) as NotificationResponse[];
        this._rawNotifications.set(content);
        this.notificationsLoading.set(false);
      }),
    );
  }
}
