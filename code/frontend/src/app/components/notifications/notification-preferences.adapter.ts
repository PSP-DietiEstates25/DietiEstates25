import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration } from '../..//services/api-configuration';
import { NotificationCategoryControllerService } from '../../services/services/notification-category-controller.service';
import { NotificationControllerService } from '../../services/services/notification-controller.service';

export type NotificationCategory =
  | 'NEW_PROPERTIES'
  | 'PROMOTIONAL'
  | 'VISIT'
  | 'OFFER';

export interface UserPref {
  category: NotificationCategory;
  enabled: boolean;
}

export interface UiNotification {
  id: number;
  message: string;
  createdDate: string;
  category: NotificationCategory;
}

const ALL_CATEGORIES: NotificationCategory[] = [
  'NEW_PROPERTIES',
  'PROMOTIONAL',
  'VISIT',
  'OFFER',
];
const LS_KEY = 'dietiestates:user-notification-prefs';

@Injectable({ providedIn: 'root' })
export class NotificationPreferencesAdapter {
  private readonly http = inject(HttpClient);
  private readonly apiCfg = inject(ApiConfiguration);
  private readonly categoryApi = inject(NotificationCategoryControllerService);
  private readonly notifApi = inject(NotificationControllerService);

  private readonly _prefs = signal<UserPref[]>(this.readLocal());
  readonly prefs = computed(() => this._prefs());

  private baseUrl(path: string) {
    return `${this.apiCfg.rootUrl}${path}`;
  }

  private readLocal(): UserPref[] {
    try {
      const raw = localStorage.getItem(LS_KEY);
      if (raw) return JSON.parse(raw);
    } catch {}
    return ALL_CATEGORIES.map((c) => ({ category: c, enabled: true }));
  }
  private persistLocal(next: UserPref[]) {
    localStorage.setItem(LS_KEY, JSON.stringify(next));
  }

  async toggle(category: NotificationCategory, enabled: boolean) {
    const next = this._prefs().map((p) =>
      p.category === category ? { ...p, enabled } : p
    );
    try {
      await this.categoryApi
        .updateIsActive({
          notificationcategoryname: category,
          body: { name: category, isActive: enabled },
        })
        .toPromise();
      this._prefs.set(next);
      this.persistLocal(next);
    } catch (e: any) {
      if (e?.status !== 404)
        console.warn('toggle pref failed, falling back to local', e);
      this._prefs.set(next);
      this.persistLocal(next);
    }
  }

  async getCategoryInfo(name: NotificationCategory) {
    return this.categoryApi
      .getNotificationCategoryByName({ notificationcategoryname: name })
      .toPromise();
  }

  async listUserNotifications(opts?: {
    page?: number;
    size?: number;
  }): Promise<UiNotification[]> {
    const page = opts?.page ?? 0;
    const size = opts?.size ?? 20;

    try {
      const url = this.baseUrl(`/notifications/me?page=${page}&size=${size}`);
      const res: any = await this.http.get(url).toPromise();
      if (res && Array.isArray(res.content)) {
        return (res.content as any[]).map((n) => ({
          id: n.id,
          message: n.message,
          createdDate: n.createdDate,
          category: (n.notificationCategory?.name ??
            n.category) as NotificationCategory,
        }));
      }
    } catch (e: any) {
      if (e?.status !== 404) {
        console.warn(
          'GET /notifications/me failed, using per-category fallback',
          e
        );
      }
    }

    const perCat = await Promise.all(
      ALL_CATEGORIES.map(async (cat) => {
        try {
          const url = `${this.apiCfg.rootUrl}/notificationcategories/${cat}/notifications/me?page=${page}&size=${size}`;
          const res: any = await this.http.get(url).toPromise();
          const content = (res?.content ?? []) as any[];
          return content.map((n) => ({
            id: n.id,
            message: n.message,
            createdDate: n.createdDate,
            category: cat,
          })) as UiNotification[];
        } catch (e) {
          console.warn(`listMine failed for ${cat}`, e);
          return [] as UiNotification[];
        }
      })
    );

    const merged = perCat.flat();
    merged.sort((a, b) => (a.createdDate < b.createdDate ? 1 : -1));
    return merged.slice(0, size);
  }
}
