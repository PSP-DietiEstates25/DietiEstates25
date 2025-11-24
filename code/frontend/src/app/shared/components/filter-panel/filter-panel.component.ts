import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormsModule, Validators } from '@angular/forms';
import {
  SearchFacade,
  Category,
} from '../../../components/search/search.facade';
import { GeographicalPositionRequest } from '../../../services/models/geographical-position-request';
import { UtilityRequest } from '../../../services/models/utility-request';
import { CadastralFilterRequest } from '../../../services/models/cadastral-filter-request';
import { switchMap } from 'rxjs';
import { Toast, ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { SearchGeographicalPosition } from '../../../interfaces/searchGeographicalPosition';

@Component({
  selector: 'app-filter-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './filter-panel.component.html',
})
export class FilterPanelComponent {

  facade = inject(SearchFacade);
  formBuilder = inject(FormBuilder);
  toastrService = inject(ToastrService);
  routerService = inject(Router);

  isOpen = signal(true);
  submitted = false;

  category: Category = 'SALE';

  geographicalPositionForm = this.formBuilder.nonNullable.group({
    state: ['' as string, [Validators.required]],
    city: ['' as string, [Validators.required]]
  });

  utilityForm = this.formBuilder.nonNullable.group({
    hasAirConditioning: [false as boolean, [Validators.required]],
    hasDoorman: [false as boolean, [Validators.required]],
    hasElevator: [false as boolean, [Validators.required]],
    nearPark: [false as boolean, [Validators.required]],
    nearSchool: [false as boolean, [Validators.required]],
    nearPublicTransport: [false as boolean, [Validators.required]],
  });

  cadastralFilterForm = this.formBuilder.nonNullable.group({
    minPrice: [0 as number, [Validators.required, Validators.min(0)]],
    maxPrice: [0 as number, [Validators.required, Validators.min(0)]],
    minSquareMeters: [0 as number, [Validators.required, Validators.min(0)]],
    maxSquareMeters: [0 as number, [Validators.required, Validators.min(0)]],
    minEnergyClass: [0 as number, [Validators.required, Validators.min(0)]],
    maxEnergyClass: [0 as number, [Validators.required, Validators.min(0)]],
    minRooms: [0 as number, [Validators.required, Validators.min(0)]],
    maxRooms: [0 as number, [Validators.required, Validators.min(0)]],
    minFloor: [0 as number, [Validators.required, Validators.min(0)]],
    maxFloor: [0 as number, [Validators.required, Validators.min(0)]],
  });

  toggleOpen() {
    this.isOpen.update((open) => !open);
  }

  apply() {
    this.submitted = true;
    //this.facade.resetContext();

    if(this.geographicalPositionForm.invalid || this.utilityForm.invalid || this.cadastralFilterForm.invalid){
      this.utilityForm.markAllAsTouched();
      this.geographicalPositionForm.markAllAsTouched();
      this.cadastralFilterForm.markAllAsTouched();
    }

    //this.facade.cacheFilters(geographicalPosition, utility, cadastralFilter);
    const geographicalPosition: SearchGeographicalPosition = {
      state: this.geographicalPositionForm.value.state,
      city: this.geographicalPositionForm.value.city,
    };

    const utility: UtilityRequest = {
      hasAirConditioning: this.utilityForm.value.hasAirConditioning as boolean,
      hasDoorman: this.utilityForm.value.hasDoorman as boolean,
      hasElevator: this.utilityForm.value.hasElevator as boolean,
      nearPark: this.utilityForm.value.nearPark as boolean,
      nearSchool: this.utilityForm.value.nearSchool as boolean,
      nearPublicTransport: this.utilityForm.value.nearPublicTransport as boolean
    };

    const cadastralFilter: CadastralFilterRequest = {
      minPrice: this.cadastralFilterForm.value.minPrice as number,
      maxPrice: this.cadastralFilterForm.value.maxPrice as number,
      minRooms: this.cadastralFilterForm.value.minRooms as number,
      maxRooms: this.cadastralFilterForm.value.maxRooms as number,
      minFloor: this.cadastralFilterForm.value.minRooms as number,
      maxFloor: this.cadastralFilterForm.value.maxRooms as number,
      minSquareMeters: this.cadastralFilterForm.value.minSquareMeters as number,
      maxSquareMeters: this.cadastralFilterForm.value.maxSquareMeters as number,
      minEnergyClass: this.cadastralFilterForm.value.minEnergyClass as number,
      maxEnergyClass: this.cadastralFilterForm.value.maxSquareMeters as number,
    };

    this.facade.prepareDetail(geographicalPosition, utility)
      .pipe(
        switchMap(() => this.facade.prepareCadastralFilter(cadastralFilter)),
        switchMap(() =>
          this.facade.search({
            category: this.category,
            page: this.page,
            size: this.size,
            userEmail: this.userEmail,
          })
        )
      )
      .subscribe({ error: () => {} });
  }

  /*
  clear() {
    this.clearSearch();
    this.clearGeographicalPosition();
    this.clearUtility();
    this.clearCadastralFilter();
    this.apply();
  }

  clearSearch(){
    this.category = 'SALE';
    this.page = 1;
    this.size = 12;
    this.userEmail = '';
  }

  clearGeographicalPosition(){
    this.geographicalPosition.set({
      city: '',
      municipality: '',
      address: '',
      latitude: 0,
      longitude: 0,
    });
  }

  clearUtility(){
    this.utility.set({
      hasAirConditioning: false,
      hasDoorman: false,
      hasElevator: false,
      nearPark: false,
      nearPublicTransport: false,
      nearSchool: false,
    });
  }

  clearCadastralFilter(){
    this.cadastralFilter.set({
      minPrice: 0,
      maxPrice: 999999999,
      minSquareMeters: 0,
      maxSquareMeters: 100000,
      minRooms: 0,
      maxRooms: 50,
      minFloor: -10,
      maxFloor: 100,
      minEnergyClass: 0,
      maxEnergyClass: 9,
    });
  }
  */
}

function coerceNumberIfNeeded<K>(k: K, v: any) {
  return typeof v === 'string' && v.trim() !== '' && !Number.isNaN(+v) ? +v : v;
}
