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

export interface UserPreferences {
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
  private readonly httpClient = inject(HttpClient);
  private readonly apiConfiguration = inject(ApiConfiguration);
  private readonly notificationCategoryService = inject(NotificationCategoryControllerService);
  private readonly notificationService = inject(NotificationControllerService);

  private readonly _userPreferences = signal<UserPreferences[]>(this.readLocal());
  readonly userPreferences = computed(() => this._userPreferences());

  private baseUrl(path: string) {
    return `${this.apiConfiguration.rootUrl}${path}`;
  }

  private readLocal(): UserPreferences[] {
    try {
      const raw = localStorage.getItem(LS_KEY);
      if (raw) return JSON.parse(raw);
    } catch {}
    return ALL_CATEGORIES.map((category) => ({ category: category, enabled: true }));
  }
  private persistLocal(next: UserPreferences[]) {
    localStorage.setItem(LS_KEY, JSON.stringify(next));
  }

  async toggle(category: NotificationCategory, enabled: boolean) {
    const next = this._userPreferences().map((preference) =>
      preference.category === category ? { ...preference, enabled } : preference
    );
    try {
      await this.notificationCategoryService.updateIsActive({
          notificationcategoryname: category,
          body: { name: category, isActive: enabled },
        })
        .toPromise();
      this._userPreferences.set(next);
      this.persistLocal(next);
    } catch (error: any) {
      if (error?.status !== 404)
        console.warn('toggle pref failed, falling back to local', error);
      this._userPreferences.set(next);
      this.persistLocal(next);
    }
  }

  async getCategoryInfo(name: NotificationCategory) {
    return this.notificationCategoryService.getNotificationCategoryByName({ notificationcategoryname: name })
      .toPromise();
  }

  //??? al posto di usare l'http client si dovrebbe usare l'apposito servizio
  // togliere questo /me di riga 93
  async listUserNotifications(params?: {
    page?: number;
    size?: number;
  }): Promise<UiNotification[]> {
    
    const page = params?.page ?? 0;
    const size = params?.size ?? 20;

    try {
      const url = this.baseUrl(`/notifications/me?page=${page}&size=${size}`);
      const response: any = await this.httpClient.get(url).toPromise();

      if (response && Array.isArray(response.content)) {
        return (response.content as any[]).map((notification) => ({
          id: notification.id,
          message: notification.message,
          createdDate: notification.createdDate,
          category: (notification.notificationCategory?.name ?? notification.category) as NotificationCategory,
        }));
      }
    } catch (error: any) {
      if (error?.status !== 404) {
        console.warn(
          'GET /notifications/me failed, using per-category fallback',
          error
        );
      }
    }

    const byNotificationCategory = await Promise.all(
      ALL_CATEGORIES.map(async (notificationCategory) => {
        try {
          const url = `${this.apiConfiguration.rootUrl}/notificationcategories/${notificationCategory}/notifications/me?page=${page}&size=${size}`;
          const res: any = await this.httpClient.get(url).toPromise();
          const content = (res?.content ?? []) as any[];
          return content.map((notification) => ({
            id: notification.id,
            message: notification.message,
            createdDate: notification.createdDate,
            category: notificationCategory,
          })) as UiNotification[];
        } catch (error) {
          console.warn(`listMine failed for ${notificationCategory}`, error);
          return [] as UiNotification[];
        }
      })
    );

    const merged = byNotificationCategory.flat();
    merged.sort((a, b) => (a.createdDate < b.createdDate ? 1 : -1));
    return merged.slice(0, size);
  }
}
