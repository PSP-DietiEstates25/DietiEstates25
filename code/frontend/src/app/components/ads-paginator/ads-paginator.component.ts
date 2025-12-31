import { Component, effect, inject } from '@angular/core';
import { AdsPaginatorService } from '../../manual_services/ads_paginator/ads-paginator.service';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-ads-paginator',
  imports: [FormsModule],
  templateUrl: './ads-paginator.component.html',
  styleUrl: './ads-paginator.component.scss',
})
export class AdsPaginatorComponent {
  adsPaginatorService = inject(AdsPaginatorService);
  routerService = inject(Router);

  adsRequest!: PaginatorRequest;
  goToPage: number = 1;
  page!: number;
  totalPages!: number;

  maxPagesVisible = environment.searchFooterMaxVisiblePages;
  displayedPages: number[] = [];

  constructor() {
    effect(() => {
      this.adsRequest = this.adsPaginatorService.adsRequest();
      this.page = this.adsPaginatorService.page();
      this.totalPages = this.adsPaginatorService.totalPagesNumber();
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
      this.handleAdsPaginatorPageClick(this.page - 1);
    }
  }

  onNext(event: Event) {
    event.preventDefault();
    if (this.page < this.totalPages) {
      this.handleAdsPaginatorPageClick(this.page + 1);
    }
  }

  handlePageNumberClick(newPage: number) {
    this.handleAdsPaginatorPageClick(newPage);
  }

  handleAdsPaginatorPageClick(newPage: number) {
    this.adsPaginatorService.setPage(newPage);
    const params: Record<string, string> = {
      size: String(5),
      page: String(this.page),
    };

    this.routerService.navigate(['/ad'], { queryParams: params });
  }
}
