import { Component, inject, OnInit, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormsModule,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { SearchFacade, Category } from '../../../components/search/search.facade';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { LocationsService } from '../../../manual_services/location/location.service';
import { AdCategory } from '../../../enums/ad-category.enum';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

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

  cadastralFilterForm = this.formBuilder.nonNullable.group(
    {
      minPrice: [0, [Validators.min(0), Validators.max(50_000_000)]],
      maxPrice: [1_000_000, [Validators.min(0), Validators.max(50_000_000)]],

      minSquareMeters: [0, [Validators.min(0), Validators.max(10_000)]],
      maxSquareMeters: [300, [Validators.min(0), Validators.max(10_000)]],

      minEnergyClass: [1, [Validators.min(1), Validators.max(10)]],
      maxEnergyClass: [10, [Validators.min(1), Validators.max(10)]],

      minRooms: [0, [Validators.min(0), Validators.max(50)]],
      maxRooms: [10, [Validators.min(0), Validators.max(50)]],

      minFloor: [0, [Validators.min(0), Validators.max(200)]],
      maxFloor: [30, [Validators.min(0), Validators.max(200)]],
    },
    {
      validators: [
        this.minLEmax('minPrice', 'maxPrice', 'priceRange'),
        this.minLEmax('minSquareMeters', 'maxSquareMeters', 'mqRange'),
        this.minLEmax('minEnergyClass', 'maxEnergyClass', 'energyRange'),
        this.minLEmax('minRooms', 'maxRooms', 'roomsRange'),
        this.minLEmax('minFloor', 'maxFloor', 'floorRange'),
      ],
    }
  );

  mainForm = this.formBuilder.group({
    geographicalPositionForm: this.geographicalPositionForm,
    utilityForm: this.utilityForm,
    cadastralFilterForm: this.cadastralFilterForm,
    category: [AdCategory.Sale as AdCategory, Validators.required],
  });

  private readonly _syncFromFacade = effect(() => {
    const gp = this.facade.cachedGeographicalPosition();
    const util = this.facade.cachedUtility();
    const cad = this.facade.cachedCadastralFilter();
    const cat = this.facade.cachedCategory();

    if (!gp || !util || !cad) return;

    this.mainForm.controls.category.setValue(
      cat === 'RENT' ? AdCategory.Rent : AdCategory.Sale,
      { emitEvent: false }
    );

    const regionCtrl = this.geographicalPositionForm.controls.region;
    const cityCtrl = this.geographicalPositionForm.controls.city;

    const region = ((gp as any).state ?? (gp as any).region ?? '') as string;
    const city = ((gp as any).city ?? '') as string;

    regionCtrl.setValue(region, { emitEvent: false });

    if (region) {
      cityCtrl.enable({ emitEvent: false });

      this.locationService.getCitiesByRegion(region).subscribe({
        next: (citiesResponse) => {
          this.cities.set(citiesResponse ?? []);
          cityCtrl.setValue(city, { emitEvent: false });
        },
        error: () => {
          cityCtrl.setValue(city, { emitEvent: false });
        },
      });
    } else {
      cityCtrl.disable({ emitEvent: false });
      cityCtrl.setValue('', { emitEvent: false });
      this.cities.set([]);
    }

    this.utilityForm.patchValue(util as any, { emitEvent: false });
    this.cadastralFilterForm.patchValue(cad as any, { emitEvent: false });
  });

  ngOnInit(): void {
    this.locationService.getRegions().subscribe({
      next: (regionsResponse) => this.regions.set(regionsResponse),
    });

    this.geographicalPositionForm.controls.region.valueChanges.subscribe({
      next: (region) => {
        const cityControl = this.geographicalPositionForm.controls.city;
        cityControl.setValue('');

        if (region) {
          cityControl.enable();
          this.locationService.getCitiesByRegion(region).subscribe({
            next: (citiesResponse) => this.cities.set(citiesResponse),
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

  touchedOrSubmitted = signal(false);

  private minLEmax(minKey: string, maxKey: string, errKey = 'range'): ValidatorFn {
    return (group: AbstractControl): ValidationErrors | null => {
      const g = group as any;
      const min = Number(g?.controls?.[minKey]?.value);
      const max = Number(g?.controls?.[maxKey]?.value);
      if (Number.isNaN(min) || Number.isNaN(max)) return null;
      return min <= max ? null : { [errKey]: true };
    };  
  }

  isInvalid(ctrl: AbstractControl | null | undefined): boolean {
    if (!ctrl) return false;
    return ctrl.invalid && (ctrl.touched || ctrl.dirty || this.touchedOrSubmitted());
  }

  errMsg(ctrl: AbstractControl | null | undefined, label = 'Campo'): string | null {
    if (!ctrl || !this.isInvalid(ctrl)) return null;

    const e = ctrl.errors ?? {};
    if (e['required']) return `${label} obbligatorio.`;
    if (e['min']) return `${label} deve essere ≥ ${e['min'].min}.`;
    if (e['max']) return `${label} deve essere ≤ ${e['max'].max}.`;
    if (e['pattern']) return `${label} non valido.`;
    return `${label} non valido.`;
  }

  apply() {
    this.touchedOrSubmitted.set(true);

    if (this.mainForm.invalid) {
      this.mainForm.markAllAsTouched();
      return;
    }

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

    const cat: Category =
      this.mainForm.controls.category.value === AdCategory.Rent ? 'RENT' : 'SALE';

    this.facade.cacheFilters(geographicalPositionRequest, utility, cadastralFilter, cat);
    this.facade.setCategory(cat);

    this.routerService.navigate(['/search']);
  }

  clearForms() {
    this.geographicalPositionForm.reset();
    this.utilityForm.reset();
    this.cadastralFilterForm.reset();
  }
}
