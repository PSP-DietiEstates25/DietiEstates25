import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificationsFacade } from '../notifications/notifications.facade';
import { NotificationCategory } from '../../enums/notification-category.enum';
import { NotificationPaginatorService } from '../../manual_services/notification_paginator/notification-paginator.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-notifications-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './notifications-list.component.html',
  styleUrl: './notifications-list.component.scss',
})
export class NotificationsListComponent implements OnInit {
  readonly facade = inject(NotificationsFacade);
  notificationPaginatorService = inject(NotificationPaginatorService);
  notifications = this.facade.notifications;
  private sessionEntryTime = 0;

  ngOnInit(): void {
    const lastSeenIso = this.facade.lastSeen();    
    this.sessionEntryTime = lastSeenIso ? new Date(lastSeenIso).getTime() : 0;
    this.facade.markAllSeen();
    this.facade.fetchBadgeData();
  }

  isNew(dateIso: string | undefined): boolean {
    if (!dateIso) return false;
    if (this.sessionEntryTime === null) return false;
    const time = new Date(dateIso).getTime();
    return time > this.sessionEntryTime;
  }

  onQuery(q: string) {
    this.facade.setQuery(q ?? '');
  }

  onToggleCategory(cat: NotificationCategory) {
    this.facade.toggleCategory(cat);
    this.notificationPaginatorService.setPage(1)
    const currentRequest =
      this.notificationPaginatorService.notificationRequest();
    currentRequest.categories = this.facade.selectedCategories();
    currentRequest.page = 1;
    this.facade.fetchNotifications(currentRequest).subscribe();
  }

  isActive(cat: NotificationCategory): boolean {
    return this.facade.isCategorySelected(cat);
  }

  onClearFilters() {
    this.facade.clearFilters();
    this.notificationPaginatorService.setPage(0);

    const req = this.notificationPaginatorService.notificationRequest();
    req.page = 1;
    req.categories = []; // Array vuoto = Tutte

    this.facade.fetchNotifications(req).subscribe();
  }

  // --- UI HELPERS ---
  labelOf(cat: NotificationCategory | string | undefined): string {
    if (!cat) return 'Generale';
    switch (cat) {
      case 'NEW_PROPERTIES':
        return 'Nuovi immobili';
      case 'PROMOTIONAL':
        return 'Promozioni';
      case 'VISIT':
        return 'Visite';
      case 'OFFER':
        return 'Offerte';
      default:
        return 'Generale';
    }
  }

  hintOf(cat: NotificationCategory | string | undefined): string {
    if (!cat) return '';
    switch (cat) {
      case 'OFFER':
        return 'Controlla i dettagli dell’offerta.';
      case 'VISIT':
        return 'Verifica data/ora.';
      case 'NEW_PROPERTIES':
        return 'Nuovi annunci per te.';
      case 'PROMOTIONAL':
        return 'Comunicazioni.';
      default:
        return '';
    }
  }

  relativeTime(iso: string | undefined): string {
    if (!iso) return '';
    const t = new Date(iso).getTime();
    const diff = Date.now() - t;
    const m = Math.floor(diff / 60000);
    if (m < 1) return 'adesso';
    if (m < 60) return `${m} min fa`;
    const h = Math.floor(m / 60);
    if (h < 24) return `${h} h fa`;
    const d = Math.floor(h / 24);
    return `${d} g fa`;
  }
}
