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
  private readonly _selectedCats = signal<Set<NotificationCategory>>(new Set());
  private readonly _query = signal<string>('');

  readonly loading = computed(() => this._loading());
  readonly prefs = this.adapter.prefs;
  readonly items = computed(() => this._pages().flatMap((p) => p.items));
  readonly unreadCount = computed(() => this.items().length);

  readonly filtered = computed(() => {
    const enabled = new Map(this.prefs().map((p) => [p.category, p.enabled]));
    const q = this._query().toLowerCase();
    const sel = this._selectedCats();
    return this.items().filter((n) => {
      if (!enabled.get(n.category)) return false;
      if (sel.size && !sel.has(n.category)) return false;
      if (q && !n.message.toLowerCase().includes(q)) return false;
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
  setQuery(q: string) {
    this._query.set(q ?? '');
  }
  toggleFilterCat(cat: NotificationCategory) {
    const s = new Set(this._selectedCats());
    s.has(cat) ? s.delete(cat) : s.add(cat);
    this._selectedCats.set(s);
  }
  clearFilters() {
    this._selectedCats.set(new Set());
    this._query.set('');
  }
}
