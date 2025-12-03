import {
  Component,
  inject,
  signal,
  OnDestroy,
  OnInit,
  effect,
  Sanitizer,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  HttpBackend,
  HttpClient,
  HttpErrorResponse,
} from '@angular/common/http';

import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FilterPanelComponent } from '../../shared/components/filter-panel/filter-panel.component';
import { RecentSearchesComponent } from '../recent-searches/recent-searches.component';

import { SearchFacade } from './search.facade';
import { SearchesPaginatorComponent } from '../searches-paginator/searches-paginator.component';
import { SearchPaginatorService } from '../../manual_services/search-paginator/search-paginator.service';
import { SearchControllerService } from '../../services/services';
import { Router, TitleStrategy } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Search, SearchResponse } from '../../services/models';
import { SearchPaginatorRequest } from '../../interfaces/search-paginator-request';

const isHttp = (s: string) => /^https?:\/\//i.test(s);
const isData = (s: string) => /^data:/i.test(s);
const looksJpeg = (b64: string) => b64?.startsWith('/9j/');
const looksPng = (b64: string) => b64?.startsWith('iVBOR');

@Component({
  selector: 'app-search-page',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    FilterPanelComponent,
    RecentSearchesComponent,
    SearchesPaginatorComponent,
  ],
  templateUrl: './search-page.component.html',
})
export class SearchPageComponent implements OnDestroy {
  facade = inject(SearchFacade);
  searchPaginatorService = inject(SearchPaginatorService);
  searchService = inject(SearchControllerService);
  routerService = inject(Router);
  toastrService = inject(ToastrService);

  savedSearches: SearchResponse[] = [];
  searchPaginatorRequest!: SearchPaginatorRequest;
  totalPages!: number;
  page!: number;

  constructor() {
    effect(() => {
      this.searchPaginatorRequest = this.searchPaginatorService.searchRequest();
      this.fetchSavedSearches();
    });
  }

  fetchSavedSearches() {
    this.facade.fetchUserSearches(this.searchPaginatorRequest).subscribe({
      next: (results) => {
        this.totalPages = results.totalPages!;
        this.savedSearches = results.content!;
        this.initPages();
      },
      error: (response: HttpErrorResponse) => {
        if (response.error === 500) {
          this.toastrService.error('Contatta un admin', 'Errore interno');
          this.routerService.navigateByUrl('/');
        }
      },
    });
  }

  initPages() {
    this.searchPaginatorService.setPagesNumber(this.totalPages);
    this.page = this.searchPaginatorService.page();
  }

  private pending = new Set<string>();

  ngOnDestroy(): void {
    this.pending.clear();
    this.searchPaginatorService.refresh();
  }

  ngOnInit() {}
}
