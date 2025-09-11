import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AdDraftService } from '../../vecchioService/ad-draft.service';

@Component({
  selector: 'app-step-details',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './step-details.component.html',
})
export class StepDetailsComponent {
  private fb = inject(FormBuilder);
  private draft = inject(AdDraftService);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    address: [this.draft.draft().address],
    type: [this.draft.draft().type || 'Appartamento'],
    size: [this.draft.draft().size, [Validators.min(0)]],
    description: [this.draft.draft().description],
  });

  constructor() {
    this.form.valueChanges.subscribe((v) => this.draft.patch(v));
  }
  back() {
    this.router.navigateByUrl('/agent/ads/new/basics');
  }
  next() {
    this.router.navigateByUrl('/agent/ads/new/photos');
  }
}
