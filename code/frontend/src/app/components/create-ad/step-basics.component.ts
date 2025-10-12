import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CreateAdFacade, Category } from './create-ad.facade';

@Component({
  selector: 'app-step-basics',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './step-basics.component.html',
})
export class StepBasicsComponent implements OnInit {
  private fb = inject(FormBuilder);
  private facade = inject(CreateAdFacade);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    category: ['SALE' as Category, Validators.required],
    description: ['', [Validators.required, Validators.minLength(3)]],
  });

  ngOnInit(): void {
    const b = this.facade.basics();
    if (b) this.form.patchValue(b, { emitEvent: false });
  }

  next() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.facade.setBasics(this.form.getRawValue());
    this.router.navigate(['/agent/ads/new/details']);
  }
}
