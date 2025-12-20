import {
  Component,
  OnInit,
  Signal,
  computed,
  effect,
  inject,
  signal,
} from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import {
  CreateAdFacade,
  PositionDraft,
  UtilitiesDraft,
} from './create-ad.facade';
import { MapComponent } from '../map/map.component';
import { ToastrService } from 'ngx-toastr';
import { GeoapifyService } from '../../manual_services/geoapify/geoapify.service';
import { DiscardDialogComponent } from '../dialog/discard-dialog/discard-dialog.component';
import { NearTag } from '../../manual_services/geoapify/geoapify.service';
import { switchMap, pipe, finalize } from 'rxjs';

@Component({
  selector: 'app-step-details',
  standalone: true,
  imports: [MapComponent, ReactiveFormsModule, DiscardDialogComponent],
  templateUrl: './step-details.component.html',
})
export class StepDetailsComponent {
  private activatedRoute = inject(ActivatedRoute);
  private geoapifyService = inject(GeoapifyService);
  private toastrService = inject(ToastrService);
  private formBuilder = inject(FormBuilder);
  private facade = inject(CreateAdFacade);
  private routerService = inject(Router);

  isLoading = signal(false);
  submitted = false;
  isDiscardModalOpen = false;

  _savedUtility!: Signal<UtilitiesDraft | null>;
  _savedGeographicalPosition!: Signal<PositionDraft | null>;

  utilitiesForm = this.formBuilder.nonNullable.group({
    hasElevator: [false],
    hasDoorman: [false],
    hasAirConditioning: [false],
    nearPark: [false],
    nearPublicTransport: [false],
    nearSchool: [false],
  });

  positionForm = this.formBuilder.nonNullable.group({
    address: ['', Validators.required],
    region: ['', Validators.required],
    city: ['', Validators.required],
    municipality: ['', Validators.required],
    latitude: [40.85631, Validators.required],
    longitude: [14.24641, Validators.required],
    radius: [0],
  });

  constructor() {
    effect(() => {
      this._savedUtility = computed(() => this.facade.getUtility());
      this._savedGeographicalPosition = computed(() =>
        this.facade.getGeographicalPosition(),
      );

      const util = this._savedUtility();
      if (util) {
        this.utilitiesForm.patchValue(
          {
            hasAirConditioning: util.hasAirConditioning,
            hasDoorman: util.hasDoorman,
            hasElevator: util.hasElevator,
          },
          { emitEvent: false },
        );
      }

      const pos = this._savedGeographicalPosition();
      if (pos) {
        this.positionForm.patchValue(
          {
            address: pos.address,
            region: pos.region,
            city: pos.city,
            municipality: pos.municipality,
            latitude: pos.latitude,
            longitude: pos.longitude,
            radius: pos.radius ?? 0,
          },
          { emitEvent: false },
        );
      }
    });
  }

  get address() {
    return this.positionForm.get('address');
  }

  get region() {
    return this.positionForm.get('region');
  }

  get municipality() {
    return this.positionForm.get('municipality');
  }

  get city() {
    return this.positionForm.get('city');
  }

  get latitude() {
    return this.positionForm.get('latitude');
  }

  get longitude() {
    return this.positionForm.get('longitude');
  }

  updateLatitude(latitude: number) {
    this.positionForm.patchValue({ latitude: latitude });
  }
  updateLongitude(longitude: number) {
    this.positionForm.patchValue({ longitude: longitude });
  }

  openDiscardModal() {
    this.isDiscardModalOpen = true;
  }

  closeDiscardModal() {
    this.isDiscardModalOpen = false;
  }

  confirmDiscard() {
    this.closeDiscardModal();
    this.facade.clearSavedData();
    this.routerService.navigate(['/']);
    this.toastrService.error('Creazione annuncio interrotta!', 'Cancellazione');
  }

  previous() {
    this.saveFormData();
    this.routerService.navigate(['../basics'], {
      relativeTo: this.activatedRoute,
    });
  }

  next() {
    this.submitted = true;

    const latitude = Number(this.positionForm.value.latitude);
    const longitude = Number(this.positionForm.value.longitude);

    this.isLoading.set(true);

    this.geoapifyService
      .getLatitudeLongitudeData(latitude, longitude)
      .pipe(
        switchMap((response: any) => {
          const result = response?.results?.[0];

          const newLatitude = result.lat;
          const newLongitude = result.lon;

          this.positionForm.patchValue({
            region: result.state,
            city: result.city,
            municipality: result.suburb ?? result.city,
            address: result.formatted,
            latitude: newLatitude,
            longitude: newLongitude,
          });

          return this.geoapifyService
            .getMunicipalityName(newLatitude, newLongitude)
            .pipe(
              switchMap((municipalityName: string) => {
                this.positionForm.patchValue({
                  region: result.state,
                  city: result.city,
                  municipality: municipalityName,
                  address: result.formatted,
                  latitude: newLatitude,
                  longitude: newLongitude,
                });

                return this.geoapifyService.getNearPlacesByLatitudeLongitude(
                  newLatitude,
                  newLongitude,
                );
              }),
            );
        }),
        finalize(() => this.isLoading.set(false))
      )
      .subscribe({
        next: (nearTagsResponse: NearTag[]) => {
          this.utilitiesForm.patchValue({
            nearPark: nearTagsResponse.includes('NEAR_PARKS'),
            nearPublicTransport: nearTagsResponse.includes(
              'NEAR_PUBLIC_TRANSPORT',
            ),
            nearSchool: nearTagsResponse.includes('NEAR_SCHOOLS'),
          });

          this.saveFormData();
          this.saveFormData();
          this.routerService.navigate(['../cadastraldata'], {
            relativeTo: this.activatedRoute,
          });
        },
        error: (error) => console.error(error),
      });
  }

  saveFormData() {
    this.facade.setUtilities(this.utilitiesForm.getRawValue());
    this.facade.setPosition(this.positionForm.getRawValue());
  }
}
