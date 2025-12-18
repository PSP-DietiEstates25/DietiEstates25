import { Component, effect, inject } from '@angular/core';
import { NotificationPaginatorService } from '../../manual_services/notification_paginator/notification-paginator.service';
import { Router } from '@angular/router';
import { NotificationPaginatorRequest } from '../../interfaces/notification-paginator-request';
import { environment } from '../../../environments/environment';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-notification-paginator',
  imports: [FormsModule],
  templateUrl: './notification-paginator.component.html',
  styleUrl: './notification-paginator.component.scss',
})
export class NotificationPaginatorComponent {
  notificationPaginatorService = inject(NotificationPaginatorService);
  routerService = inject(Router);

  notificationRequest!: NotificationPaginatorRequest;
  goToPage!: number;
  page!: number;
  totalPages!: number;

  maxPagesVisible = environment.searchFooterMaxVisiblePages;
  displayedPages: number[] = [];

  constructor() {
    effect(() => {
      this.notificationRequest =
        this.notificationPaginatorService.notificationRequest();
      this.page = this.notificationPaginatorService.page();
      this.totalPages = this.notificationPaginatorService.totalPagesNumber();
      this.displayedPages = this.getVisiblePages(
        this.totalPages,
        this.page,
        this.maxPagesVisible,
      );
    });
  }

  getVisiblePages(
    totalPages: number,
    current: number,
    maxVisible = this.maxPagesVisible,
  ): number[] {
    if (totalPages === 0) return [];

    if (totalPages <= maxVisible) {
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    }

    const half = Math.floor(maxVisible / 2);
    let start = current - half;
    let end = start + maxVisible - 1;

    if (start < 1) {
      start = 1;
      end = start + maxVisible - 1;
    }

    if (end > totalPages) {
      end = totalPages;
      start = end - maxVisible + 1;
    }

    const pages: number[] = [];
    for (let i = start; i <= end; i++) pages.push(i);

    return pages;
  }

  onPrev(event: Event) {
    event.preventDefault();
    if (this.page > 1) {
      this.handleNotificationPaginatorPageClick(this.page - 1);
    }
  }

  onNext(event: Event) {
    event.preventDefault();
    if (this.page < this.totalPages) {
      this.handleNotificationPaginatorPageClick(this.page + 1);
    }
  }

  handlePageNumberClick(newPage: number) {
    this.handleNotificationPaginatorPageClick(newPage);
  }

  handleNotificationPaginatorPageClick(newPage: number) {
    this.notificationPaginatorService.setPage(newPage);
    const params: Record<string, string> = {
      size: String(5),
      page: String(this.page),
      category: String(this.notificationRequest.category),
    };

    this.routerService.navigate(['/notifications'], { queryParams: params });
  }
}
