import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AdDraftService } from '../../vecchioService/ad-draft.service';

import { MapComponent } from "../map/map.component";

@Component({
  selector: 'app-step-details',
  standalone: true,
  imports: [ReactiveFormsModule, MapComponent],
  templateUrl: './step-details.component.html',
})
export class StepDetailsComponent {
  private fb = inject(FormBuilder);
  private draft = inject(AdDraftService);
  private router = inject(Router);

  submitted = false;

  form = this.fb.nonNullable.group({
    // ✅ latitudine e longitudine prese dal draft e con validatori
    latitude: [
      this.draft.draft().latitude,
      [Validators.required, Validators.min(-90), Validators.max(90)],
    ],
    longitude: [
      this.draft.draft().longitude,
      [Validators.required, Validators.min(-180), Validators.max(180)],
    ],
    type: [this.draft.draft().type || 'Appartamento'],
    size: [this.draft.draft().size, [Validators.min(0)]],
    description: [this.draft.draft().description],
  });

  constructor() {
    // ogni modifica del form aggiorna il draft
    this.form.valueChanges.subscribe((v) => this.draft.patch(v));
  }

  // ✅ collegati agli output della mappa
  updateLatitude(latitude: number) {
    this.form.patchValue({ latitude });
  }

  updateLongitude(longitude: number) {
    this.form.patchValue({ longitude });
  }

  back() {
    this.router.navigateByUrl('/agent/ads/new/basics');
  }

  next() {
    this.submitted = true;
    if (this.form.valid) {
      this.router.navigateByUrl('/agent/ads/new/photos');
    }
  }
}
