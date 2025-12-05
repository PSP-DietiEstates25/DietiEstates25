import { Component, effect, inject } from '@angular/core';
import { SearchControllerService } from '../../services/services';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { FormsModule } from '@angular/forms';
import { OffersPaginatorService } from '../../manual_services/offers_paginator/offers-paginator.service';

@Component({
  selector: 'app-offers-paginator',
  imports: [FormsModule],
  templateUrl: './offers-paginator.component.html',
  styleUrl: './offers-paginator.component.scss',
})
export class OffersPaginatorComponent {
  offerService = inject(SearchControllerService);
  offerPaginatorService = inject(OffersPaginatorService);
  routerService = inject(Router);

  offersRequest!: PaginatorRequest;
  goToPage!: number;
  page!: number;
  totalPages!: number;

  maxPagesVisible = environment.searchFooterMaxVisiblePages;
  displayedPages: number[] = [];

  constructor() {
    effect(() => {
      this.offersRequest = this.offerPaginatorService.offerRequest();
      this.page = this.offerPaginatorService.page();
      this.totalPages = this.offerPaginatorService.totalPagesNumber();
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
      this.handleOfferPaginatorPageClick(this.page - 1);
    }
  }

  onNext(event: Event) {
    event.preventDefault();
    if (this.page < this.totalPages) {
      this.handleOfferPaginatorPageClick(this.page + 1);
    }
  }

  handlePageNumberClick(newPage: number) {
    this.handleOfferPaginatorPageClick(newPage);
  }

  handleOfferPaginatorPageClick(newPage: number) {
    this.offerPaginatorService.setPage(newPage);
    const params: Record<string, string> = {
      size: String(5),
      page: String(this.page),
    };

    this.routerService.navigate(['/offers'], { queryParams: params });
  }
}
