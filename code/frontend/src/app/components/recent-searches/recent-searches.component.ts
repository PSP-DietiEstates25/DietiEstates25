import { Component, inject, Signal, computed, effect } from '@angular/core';
import { DatePipe } from '@angular/common';
import { SearchFacade } from '../../components/search/search.facade';
import { Search, SearchResponse } from '../../services/models';
import { SearchPaginatorService } from '../../manual_services/search-paginator/search-paginator.service';
import { SearchControllerService } from '../../services/services';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../../environments/environment.development';
import { FullRealEstate } from '../../interfaces/full-real-estate';
import { FullSearch } from '../../interfaces/full-search';

@Component({
  selector: 'app-recent-searches',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './recent-searches.component.html',
})
export class RecentSearchesComponent {
  facade = inject(SearchFacade);
  savedSearches!: Signal<FullSearch[]>;
  searchService = inject(SearchControllerService);
  searchPaginatorService = inject(SearchPaginatorService);
  toastrService = inject(ToastrService);

  constructor() {
    effect(() => {
      this.savedSearches = computed(() => this.facade.savedSearches());
      console.log(this.savedSearches());
    });
  }

  getImageUrl(path?: string) {
    return `${environment.apiBaseUrl}${path}`;
  }

  onClick() {}

  replay(search: FullSearch) {
    this.facade.replaySearch(search);
  }
}
