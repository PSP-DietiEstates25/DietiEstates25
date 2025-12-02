import { Component, inject, input } from '@angular/core';
import { Search } from '../../services/models';
import { Router } from '@angular/router';
import { SearchControllerService } from '../../services/services';
import { ToastrService } from 'ngx-toastr';
import { SearchPaginatorService } from '../../manual_services/search-paginator/search-paginator.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-saved-searches-list',
  imports: [],
  templateUrl: './saved-searches-list.component.html',
  styleUrl: './saved-searches-list.component.scss',
})
export class SavedSearchesListComponent {
  routerService = inject(Router);
  searchService = inject(SearchControllerService);
  toastrService = inject(ToastrService);
  searchPaginatorService = inject(SearchPaginatorService);

  searches = input<Search[]>([]);

  getMainImageUrl(path: string) {
    return `${environment.apiBaseUrl}${path}`;
  }

  onClick(searchId: number) {
    //quando l'utente clicca su questa ricerca, lo porta alla pagina con tutti gli immobili a sx e la mappa centrale che li mostra
  }
}
