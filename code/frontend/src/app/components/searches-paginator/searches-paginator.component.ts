import { Component, inject, effect } from '@angular/core';
import { SearchControllerService } from '../../services/services';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { FormsModule } from '@angular/forms';
import { SearchPaginatorService } from '../../manual_services/search-paginator/search-paginator.service';
import { SearchPaginatorRequest } from '../../interfaces/search-paginator-request';

@Component({
  selector: 'app-searches-paginator',
  imports: [FormsModule],
  templateUrl: './searches-paginator.component.html',
  styleUrl: './searches-paginator.component.scss',
})
export class SearchesPaginatorComponent {
  searchesService = inject(SearchControllerService);
  searchPaginatorService = inject(SearchPaginatorService);
  routerService = inject(Router);

  searchRequest!: SearchPaginatorRequest;
  goToPage!: number;
  page!: number;
  totalPages!: number;

  maxPagesVisible = environment.searchFooterMaxVisiblePages;
  displayedPages: number[] = [];

  constructor() {
    effect(() => {
      this.searchRequest = this.searchPaginatorService.searchRequest();
      this.page = this.searchPaginatorService.page();
      this.totalPages = this.searchPaginatorService.totalPagesNumber();
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
      this.handleSearchPaginatorPageClick(this.page - 1);
    }
  }

  onNext(event: Event) {
    event.preventDefault();
    if (this.page < this.totalPages) {
      this.handleSearchPaginatorPageClick(this.page + 1);
    }
  }

  handlePageNumberClick(newPage: number) {
    this.handleSearchPaginatorPageClick(newPage);
  }

  handleSearchPaginatorPageClick(newPage: number) {
    this.searchPaginatorService.setPage(newPage);
    const params: Record<string, string> = {
      size: String(5),
      page: String(this.page),
    };

    this.routerService.navigate(['/searches'], { queryParams: params });
  }
}
