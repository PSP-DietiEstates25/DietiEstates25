import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';

@Component({
  selector: 'app-step-basics',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './step-basics.component.html',
})
export class StepBasicsComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private facade = inject(CreateAdFacade);

  form = this.fb.nonNullable.group({
    title: [
      this.facade.draft().title ?? '',
      [Validators.required, Validators.minLength(3)],
    ],
    price: [
      this.facade.draft().price ?? 0,
      [Validators.required, Validators.min(0)],
    ],
    city: [this.facade.draft().city ?? '', [Validators.required]],

    rooms: [this.facade.draft().rooms ?? 0, [Validators.min(0)]],
    floor: [this.facade.draft().floor ?? 0, [Validators.min(0)]],
    energyClass: [
      this.facade.draft().energyClass ?? 'ND',
      [Validators.required],
    ],
  });

  constructor() {
    this.form.valueChanges.subscribe((v) => this.facade.patchBasics(v));
  }

  next() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.router.navigateByUrl('/agent/ads/new/details');
  }
}
