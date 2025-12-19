import {
  Component,
  effect,
  HostListener,
  inject,
  OnDestroy,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificationsFacade } from './notifications.facade';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { NotificationsListComponent } from '../notifications-list/notifications-list.component';
import { NotificationPaginatorComponent } from '../notification-paginator/notification-paginator.component';
import { NotificationPaginatorService } from '../../manual_services/notification_paginator/notification-paginator.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { NotificationResponse } from '../../services/models';
import { NotificationPaginatorRequest } from '../../interfaces/notification-paginator-request';
import { NotificationCategory } from '../../enums/notification-category.enum';

@Component({
  selector: 'app-notifications-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NavbarComponent,
    NotificationsListComponent,
    NotificationPaginatorComponent,
  ],
  templateUrl: './notifications-page.component.html',
})
export class NotificationsPageComponent implements OnDestroy {
  facade = inject(NotificationsFacade);
  notificationPaginatorService = inject(NotificationPaginatorService);
  routerService = inject(Router);
  toastrService = inject(ToastrService);

  notifications: NotificationResponse[] = [];
  notificationPaginatorRequest!: NotificationPaginatorRequest;
  totalPages!: number;
  page!: number;

  notificationCategories = new Map<string, string>([
    ['NEW_PROPERTIES', 'Nuovi annunci'],
    ['PROMOTIONAL', 'Promozioni'],
    ['VISIT', 'Visite'],
    ['OFFER', 'Offerte'],
  ]);

  notificationCategoriesFilter!: NotificationCategory[] | null;

  constructor() {
    effect(() => {
      this.notificationPaginatorRequest =
        this.notificationPaginatorService.notificationRequest();
      this.notificationCategoriesFilter =
        this.notificationPaginatorRequest.categories;
      this.fetchUserNotifications();
    });
  }

  fetchUserNotifications() {
    this.facade
      .fetchNotifications(this.notificationPaginatorRequest)
      .subscribe({
        next: (results) => {
          this.totalPages = results.totalPages!;
          this.notifications = results.content!;
          this.initPages();
        },
      });
  }

  initPages() {
    this.notificationPaginatorService.setPagesNumber(this.totalPages);
    this.page = this.notificationPaginatorService.page();
  }

  ngOnDestroy(): void {
    this.notificationPaginatorService.refresh();
  }
}
