import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { MapComponent } from '../map/map.component';

@Component({
  selector: 'app-step-details',
  standalone: true,
  imports: [ReactiveFormsModule, MapComponent],
  templateUrl: './step-details.component.html',
})
export class StepDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);
  private facade = inject(CreateAdFacade);
  private router = inject(Router);

  utilitiesForm = this.fb.nonNullable.group({
    hasElevator: [false],
    hasDoorman: [false],
    hasAirConditioning: [false],

    nearPark: [false],
    nearPublicTransport: [false],
    nearSchool: [false],
  });

  positionForm = this.fb.nonNullable.group({
    address: ['', Validators.required],
    city: ['', Validators.required],
    municipality: ['', Validators.required],
    latitude: [40.85631, Validators.required],
    longitude: [14.24641, Validators.required],
    radius: [0],
  });

  ngOnInit(): void {
    const u = this.facade.utilities();
    if (u) this.utilitiesForm.patchValue(u, { emitEvent: false });

    const p = this.facade.position();
    if (p) this.positionForm.patchValue(p, { emitEvent: false });
  }

  onLatChange(lat: number) {
    this.positionForm.patchValue({ latitude: lat });
  }
  onLngChange(lng: number) {
    this.positionForm.patchValue({ longitude: lng });
  }

  next() {
    if (this.utilitiesForm.invalid || this.positionForm.invalid) {
      this.utilitiesForm.markAllAsTouched();
      this.positionForm.markAllAsTouched();
      return;
    }
    this.facade.setUtilities(this.utilitiesForm.getRawValue());
    this.facade.setPosition(this.positionForm.getRawValue());
    this.router.navigate(['../cadastral'], { relativeTo: this.route });
  }
}
