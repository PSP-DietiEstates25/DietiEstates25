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

  private readonly _pageNotifications = signal<NotificationResponse[]>([]);
  private readonly _badgeNotifications = signal<NotificationResponse[]>([]);

  readonly selectedCategories = signal<NotificationCategory[]>([]);
  readonly searchQuery = signal<string>('');
  readonly allCategories = ALL_NOTIFICATION_CATEGORIES;

  readonly notifications = computed(() => {
    const all = this._pageNotifications();
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

  // Inizializzazione: Leggiamo direttamente dal Service
  private readonly _lastSeen = signal<string | null>(
    this.localStorage.getItem(this.LAST_SEEN_KEY)
  );
  readonly lastSeen = this._lastSeen.asReadonly();

  readonly unreadCount = computed(() => {
    const lastSeenIso = this._lastSeen();
    const latestItems = this._badgeNotifications();
    
    // Se lastSeenIso è null (mai visitato), tecnicamente sono tutte non lette.
    // Il componente visuale gestirà il fatto di non mostrare pallini verdi al primo avvio.
    if (!lastSeenIso) return latestItems.length;
    
    const last = new Date(lastSeenIso).getTime();
    return latestItems.filter((n) => {
      const d = n.createdDate || n.lastModifiedDate;
      const t = d ? new Date(d).getTime() : 0;
      return t > last;
    }).length;
  });

  // --- METODI --
  markAllSeen(): void {
    let now = new Date().getTime();

    // Recuperiamo tutte le notifiche attualmente in memoria per calcolare il tempo massimo
    const pageItems = this._pageNotifications();
    const badgeItems = this._badgeNotifications();
    const allItems = [...pageItems, ...badgeItems];

    let maxNotificationTime = 0;
    allItems.forEach((n) => {
      if (n.createdDate) {
        const t = new Date(n.createdDate).getTime();
        if (t > maxNotificationTime) maxNotificationTime = t;
      }
    });

    // CORREZIONE CRITICA:
    // Se l'ultima notifica è nel "futuro" o uguale ad adesso (disallineamento server/client),
    // forziamo il "lastSeen" a 1 millisecondo DOPO quella notifica.
    if (maxNotificationTime >= now) {
      now = maxNotificationTime + 1;
    }

    const isoString = new Date(now).toISOString();
    console.log('[Facade] Updating Last Seen via Service to:', isoString);

    // 1. Aggiorna il Signal (Memoria immediata)
    this._lastSeen.set(isoString);
    
    // 2. Scrive tramite il tuo Service (Persistenza)
    this.localStorage.setItem(this.LAST_SEEN_KEY, isoString);
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
      const validCats = request.categories.filter((category) => category !== null);
      if (validCats.length > 0) params.categories = validCats;
    }
    return this.notificationService.getUserNotifications(params);
  }

  fetchNotifications(request: NotificationPaginatorRequest) {
    this.notificationsLoading.set(true);
    return this.getNotifications(request).pipe(
      switchMap((response) => of(response)),
      tap((response) => {
        const content = (response.content || []) as NotificationResponse[];
        this._pageNotifications.set(content);
        this.notificationsLoading.set(false);
      }),
    );
  }

  fetchBadgeData() {
    const request: NotificationPaginatorRequest = {
        page: 1, 
        size: 5, 
        categories: [] 
    };
    
    this.getNotifications(request).subscribe((response) => {
       const content = (response.content || []) as NotificationResponse[];
       this._badgeNotifications.set(content);
    });
  }
}
