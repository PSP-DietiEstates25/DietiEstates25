import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormsModule,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import {
  SearchFacade,
  Category,
} from '../../../components/search/search.facade';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { LocationsService } from '../../../manual_services/location.service';
import { AdCategory } from '../../../enums/ad-category.enum';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-filter-panel',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './filter-panel.component.html',
})
export class FilterPanelComponent implements OnInit {
  facade = inject(SearchFacade);
  formBuilder = inject(FormBuilder);
  locationService = inject(LocationsService);
  toastrService = inject(ToastrService);
  routerService = inject(Router);

  isOpen = signal(true);

  regions = signal<string[]>([]);
  cities = signal<string[]>([]);

  geographicalPositionForm = this.formBuilder.nonNullable.group({
    region: ['' as string, [Validators.required]],
    city: [{ value: '' as string, disabled: true }, [Validators.required]],
  });

  utilityForm = this.formBuilder.nonNullable.group({
    hasAirConditioning: [false],
    hasDoorman: [false],
    hasElevator: [false],
    nearPark: [false],
    nearSchool: [false],
    nearPublicTransport: [false],
  });

  cadastralFilterForm = this.formBuilder.nonNullable.group({
    minPrice: [0],
    maxPrice: [0],
    minSquareMeters: [0],
    maxSquareMeters: [0],
    minEnergyClass: [0],
    maxEnergyClass: [0],
    minRooms: [0],
    maxRooms: [0],
    minFloor: [0],
    maxFloor: [0],
  });

  mainForm = this.formBuilder.group({
    geographicalPositionForm: this.geographicalPositionForm,
    utilityForm: this.utilityForm,
    cadastralFilterForm: this.cadastralFilterForm,
    category: [AdCategory.Sale as AdCategory, Validators.required],
  });

  ngOnInit(): void {
    this.locationService.getRegions().subscribe({
      next: (regionsResponse) => {
        this.regions.set(regionsResponse);
      },
    });

    this.geographicalPositionForm.controls.region.valueChanges.subscribe({
      next: (region) => {
        const cityControl = this.geographicalPositionForm.controls.city;
        cityControl.setValue('');

        if (region) {
          cityControl.enable();
          this.locationService.getCitiesByRegion(region).subscribe({
            next: (citiesResponse) => {
              this.cities.set(citiesResponse);
            },
          });
        } else {
          cityControl.disable();
          this.cities.set([]);
        }
      },
    });
  }

  toggleOpen() {
    this.isOpen.update((open) => !open);
  }

  apply() {
    if (this.geographicalPositionForm.invalid) {
      this.geographicalPositionForm.markAllAsTouched();
      return;
    }

    const geographicalPosition = this.geographicalPositionForm.getRawValue();
    const utility = this.utilityForm.getRawValue();
    const cadastralFilter = this.cadastralFilterForm.getRawValue();

    const geographicalPositionRequest = {
      state: geographicalPosition.region,
      city: geographicalPosition.city,
      municipality: '',
      address: '',
      latitude: 0,
      longitude: 0,
    };

    this.facade.cacheFilters(
      geographicalPositionRequest,
      utility,
      cadastralFilter,
    );

    this.facade
      .prepareDetail(geographicalPositionRequest, utility)
      .pipe(
        switchMap(() => this.facade.prepareCadastralFilter(cadastralFilter)),
      );

    this.routerService.navigate(['/search']);
    //this.facade.resetContext();
    /*
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
    */
  }

  clearForms() {
    this.geographicalPositionForm.reset();
    this.utilityForm.reset();
    this.cadastralFilterForm.reset();
  }
}
