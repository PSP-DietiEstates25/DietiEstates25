import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { MapComponent } from '../map/map.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-step-details',
  standalone: true,
  imports: [ReactiveFormsModule, MapComponent],
  templateUrl: './step-details.component.html',
})
export class StepDetailsComponent implements OnInit {

  private activatedRoute = inject(ActivatedRoute);
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

  onDiscard(){
    this.routerService.navigate(['/']);
    this.toastrService.error('Creazione annuncio interrotta!', "Cancellazione");
  }

  previous(){
    this.routerService.navigateByUrl('/basics');
  }

  next() {
    this.submitted = true;

    if (this.utilitiesForm.invalid || this.positionForm.invalid) {
      this.utilitiesForm.markAllAsTouched();
      this.positionForm.markAllAsTouched();
      return;
    }

    console.log("Latitude: " + this.positionForm.value.latitude);
    console.log("Longitude: " + this.positionForm.value.longitude);

    this.facade.setUtilities(this.utilitiesForm.getRawValue());
    this.facade.setPosition(this.positionForm.getRawValue());
    this.routerService.navigate(['/cadastraldata'], { relativeTo: this.activatedRoute });
  }
}
