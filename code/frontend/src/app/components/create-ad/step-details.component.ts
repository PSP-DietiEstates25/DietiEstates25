// src/app/components/create-ad/step-details.component.ts
import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { MapComponent } from '../map/map.component';

@Component({
  selector: 'app-step-details',
  standalone: true,
  imports: [ReactiveFormsModule, MapComponent],
  templateUrl: './step-details.component.html',
})
export class StepDetailsComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private facade = inject(CreateAdFacade);

  private readonly DEF_LAT = 40.85631;
  private readonly DEF_LON = 14.24641;

  form = this.fb.nonNullable.group({
    type: [
      this.facade.draft().category ?? 'Appartamento',
      [Validators.required],
    ],
    size: [
      this.facade.draft().size ?? 0,
      [Validators.required, Validators.min(0)],
    ],
    description: [this.facade.draft().description ?? ''],
    latitude: [
      this.facade.draft().latitude ?? this.DEF_LAT,
      [Validators.required, Validators.min(-90), Validators.max(90)],
    ],
    longitude: [
      this.facade.draft().longitude ?? this.DEF_LON,
      [Validators.required, Validators.min(-180), Validators.max(180)],
    ],
  });

  constructor() {
    this.form.valueChanges.subscribe((v) => {
      this.facade.patchDetails({
        category: v.type, 
        size: v.size, 
        description: v.description, 
        latitude: v.latitude, 
        longitude: v.longitude, 
      });
    });
  }

  updateLatitude(lat: number) {
    this.form.patchValue({ latitude: lat }, { emitEvent: true });
  }
  updateLongitude(lon: number) {
    this.form.patchValue({ longitude: lon }, { emitEvent: true });
  }

  back() {
    this.router.navigateByUrl('/agent/ads/new/basics');
  }

  next() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.router.navigateByUrl('/agent/ads/new/photos');
  }
}
