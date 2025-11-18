import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { MapComponent } from '../map/map.component';
import { ToastrService } from 'ngx-toastr';
import { GeoapifyService } from '../../services/services/geoapify.service';

@Component({
  selector: 'app-step-details',
  standalone: true,
  imports: [MapComponent, ReactiveFormsModule],
  templateUrl: './step-details.component.html',
})
export class StepDetailsComponent implements OnInit {

  private activatedRoute = inject(ActivatedRoute);
  private geoapifyService = inject(GeoapifyService);
  private toastrService = inject(ToastrService);
  private formBuilder = inject(FormBuilder);
  private facade = inject(CreateAdFacade);
  private routerService = inject(Router);

  submitted = false;

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
    city: ['', Validators.required],
    municipality: ['', Validators.required],
    latitude: [40.85631, Validators.required],
    longitude: [14.24641, Validators.required],
    radius: [0],
  });

  ngOnInit(): void {
    const utility = this.facade.utility();
    if (utility) this.utilitiesForm.patchValue(utility, { emitEvent: false });

    const geographicalPosition = this.facade.geographicalPosition();
    if (geographicalPosition) this.positionForm.patchValue(geographicalPosition, { emitEvent: false });
  }

  get address(){
    return this.positionForm.get('address');
  }

  get municipality(){
    return this.positionForm.get('municipality');
  }

  get city(){
    return this.positionForm.get('city');
  }

  get latitude(){
    return this.positionForm.get('latitude');
  }

  get longitude(){
    return this.positionForm.get('longitude');
  }

  updateLatitude(latitude: number) {
    this.positionForm.patchValue({ latitude: latitude });
  }
  updateLongitude(longitude: number) {
    this.positionForm.patchValue({ longitude: longitude });
  }

  discard(){
    this.routerService.navigate(['/']);
    this.toastrService.error('Creazione annuncio interrotta!', "Cancellazione");
  }

  previous(){
    this.routerService.navigateByUrl('/basics');
  }

  next() {
    this.submitted = true;
    const latitude = this.positionForm.value.latitude as number;
    const longitude = this.positionForm.value.longitude as number;

    /*
    if (this.utilitiesForm.invalid || this.positionForm.invalid) {
      this.utilitiesForm.markAllAsTouched();
      this.positionForm.markAllAsTouched();
      return;
    }
    */

    this.geoapifyService.getLatitudeLongitudeData(latitude, longitude).subscribe({
      next: (data: any) => {
        this.positionForm.patchValue({
          city: data.results[0].city,
          municipality: data.results[0].suburb,
          address: data.results[0].formatted,

          //ATTENZIONE: LATITUDE E LONGITUDE VENGONO SOVRASCRITTI CON QUELLI TROVATI DA GEOAPIFY, QUINDI POTREBBERO NON COINCIDERE CON QUELLI ORIGINALI
          latitude: data.results[0].lat,
          longitude: data.results[0].lon,
        });

        this.facade.setUtilities(this.utilitiesForm.getRawValue());
        this.facade.setPosition(this.positionForm.getRawValue());
        this.routerService.navigate(['/cadastraldata'], { relativeTo: this.activatedRoute });
      },
    });
  }
}
