import { Component, effect, inject } from '@angular/core';
import { OffersPaginatorService } from '../../manual_services/offers_paginator/offers-paginator.service';
import { Router } from '@angular/router';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { environment } from '../../../environments/environment';
import { VisitPaginatorService } from '../../manual_services/visit_paginator/visit-paginator.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-visits-paginator',
  imports: [FormsModule],
  templateUrl: './visits-paginator.component.html',
  styleUrl: './visits-paginator.component.scss',
})
export class VisitsPaginatorComponent {
  visitPaginatorService = inject(VisitPaginatorService);
  routerService = inject(Router);

  visitRequest!: PaginatorRequest;
  goToPage!: number;
  page!: number;
  totalPages!: number;

  maxPagesVisible = environment.searchFooterMaxVisiblePages;
  displayedPages: number[] = [];

  constructor() {
    effect(() => {
      this.visitRequest = this.visitPaginatorService.visitRequest();
      this.page = this.visitPaginatorService.page();
      this.totalPages = this.visitPaginatorService.totalPagesNumber();
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
      this.handleVisitPaginatorPageClick(this.page - 1);
    }
  }

  onNext(event: Event) {
    event.preventDefault();
    if (this.page < this.totalPages) {
      this.handleVisitPaginatorPageClick(this.page + 1);
    }
  }

  handlePageNumberClick(newPage: number) {
    this.handleVisitPaginatorPageClick(newPage);
  }

  handleVisitPaginatorPageClick(newPage: number) {
    this.visitPaginatorService.setPage(newPage);
    const params: Record<string, string> = {
      size: String(5),
      page: String(this.page),
    };

    this.routerService.navigate(['/visits'], { queryParams: params });
  }
}
