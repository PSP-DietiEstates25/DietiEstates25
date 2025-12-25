import { Component, inject, Signal, computed, effect } from '@angular/core';
import { DatePipe } from '@angular/common';
import { SearchFacade } from '../../components/search/search.facade';
import { SearchPaginatorService } from '../../manual_services/search-paginator/search-paginator.service';
import { SearchControllerService } from '../../services/services';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../../environments/environment';
import { FullSearch } from '../../interfaces/full-search';
import { PriceIconComponent } from '../../shared/icons/price-icon/price-icon.component';
import { SquareMetersIconComponent } from '../../shared/icons/square-meters-icon/square-meters-icon.component';
import { RoomsIconComponent } from '../../shared/icons/rooms-icon/rooms-icon.component';
import { FloorIconComponent } from '../../shared/icons/floor-icon/floor-icon.component';
import { EnergyClassIconComponent } from '../../shared/icons/energy-class-icon/energy-class-icon.component';
import { ElevatorIconComponent } from '../../shared/icons/elevator-icon/elevator-icon.component';
import { AirConditioningIconComponent } from '../../shared/icons/air-conditioning-icon/air-conditioning-icon.component';
import { DoormanIconComponent } from '../../shared/icons/doorman-icon/doorman-icon.component';
import { NearParkIconComponent } from '../../shared/icons/near-park-icon/near-park-icon.component';
import { NearPublicTransportIconComponent } from '../../shared/icons/near-public-transport-icon/near-public-transport-icon.component';
import { NearSchoolIconComponent } from '../../shared/icons/near-school-icon/near-school-icon.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-recent-searches',
  standalone: true,
  imports: [
    DatePipe,
    PriceIconComponent,
    SquareMetersIconComponent,
    RoomsIconComponent,
    FloorIconComponent,
    EnergyClassIconComponent,
    ElevatorIconComponent,
    AirConditioningIconComponent,
    DoormanIconComponent,
    NearParkIconComponent,
    NearPublicTransportIconComponent,
    NearSchoolIconComponent,
  ],
  templateUrl: './recent-searches.component.html',
})
export class RecentSearchesComponent {
  facade = inject(SearchFacade);
  savedSearches!: Signal<FullSearch[]>;
  searchService = inject(SearchControllerService);
  searchPaginatorService = inject(SearchPaginatorService);
  toastrService = inject(ToastrService);
  routerService = inject(Router);

  constructor() {
    effect(() => {
      this.savedSearches = computed(() => this.facade.savedSearches());
      console.log(this.savedSearches());
    });
  }

  getImageUrl(path?: string) {
    return `${environment.apiBaseUrl}${path}`;
  }

  replay(search: FullSearch) {
    this.facade.replaySearch(search).subscribe({
      next: () => {
        this.toastrService.success('Ricerca caricata con successo');
        this.routerService.navigate(['/search']);
      },
      error: (err) => {
        console.error(err);
        this.toastrService.error('Impossibile rieseguire la ricerca');
      },
    });
  }
}
