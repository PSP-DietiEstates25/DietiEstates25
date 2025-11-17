import { inject, Injectable, signal, computed } from '@angular/core';
import {
  NotificationPreferencesAdapter,
  NotificationCategory,
  UiNotification, // NEW
} from './notification-preferences.adapter';

type Page = { items: UiNotification[]; page: number; size: number };

@Injectable({ providedIn: 'root' })
export class NotificationsFacade {
  private readonly adapter = inject(NotificationPreferencesAdapter);

  private readonly _loading = signal(false);
  private readonly _pages = signal<Page[]>([]);
  private readonly _page = signal(0);
  private readonly _size = signal(20);
  private readonly _selectedNotificationCategories = signal<Set<NotificationCategory>>(new Set());
  private readonly _query = signal<string>('');

  readonly loading = computed(() => this._loading());
  readonly userPreferences = this.adapter.userPreferences;
  readonly items = computed(() => this._pages().flatMap((page) => page.items));
  readonly unreadCount = computed(() => this.items().length);

  readonly filtered = computed(() => {
    const enabledCategories = new Map(this.userPreferences().map((preference) => [preference.category, preference.enabled]));
    const query = this._query().toLowerCase();
    const selectedNotificationCategories = this._selectedNotificationCategories();
    return this.items().filter((notification) => {
      if (!enabledCategories.get(notification.category)) return false;
      if (selectedNotificationCategories.size && !selectedNotificationCategories.has(notification.category)) return false;
      if (query && !notification.message.toLowerCase().includes(query)) return false;
      return true;
    });
  });

  async init() {
    this._loading.set(true);
    try {
      this._pages.set([]); 
      this._page.set(0);
      await this.loadPage(0);
    } finally {
      this._loading.set(false);
    }
  }

  private async loadPage(page: number) {
    const size = this._size();
    const items = await this.adapter.listUserNotifications({ page, size });
    const next = this._pages().slice();
    next.push({ items, page, size });
    this._pages.set(next);
    this._page.set(page);
  }

  async loadMore() {
    if (this._loading()) return; 
    this._loading.set(true);
    try {
      await this.loadPage(this._page() + 1);
    } finally {
      this._loading.set(false);
    }
  }

  async setCategoryEnabled(category: NotificationCategory, enabled: boolean) {
    await this.adapter.toggle(category, enabled);
  }

  setQuery(query: string) {
    this._query.set(query ?? '');
  }

  toggleFilterCat(notificationCategory: NotificationCategory) {
    const selectedNotificationCategories = new Set(this._selectedNotificationCategories());
    selectedNotificationCategories.has(notificationCategory) ? selectedNotificationCategories.delete(notificationCategory) : selectedNotificationCategories.add(notificationCategory);
    this._selectedNotificationCategories.set(selectedNotificationCategories);
  }

  clearFilters() {
    this._selectedNotificationCategories.set(new Set());
    this._query.set('');
  }
}
