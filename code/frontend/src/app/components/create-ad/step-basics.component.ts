import { Component, inject, signal, effect } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AdDraftService } from '../../vecchioService/ad-draft.service';

@Component({
  selector: 'app-step-basics',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './step-basics.component.html',
})
export class StepBasicsComponent {
  private fb = inject(FormBuilder);
  private draft = inject(AdDraftService);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    title: [
      this.draft.draft().title,
      [Validators.required, Validators.minLength(3)],
    ],
    price: [this.draft.draft().price, [Validators.required, Validators.min(0)]],
    city: [this.draft.draft().city, [Validators.required]],
  });

  constructor() {
    this.form.valueChanges.subscribe((v) => this.draft.patch(v));
  }

  next() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.router.navigateByUrl('/agent/ads/new/details');
  }
}
