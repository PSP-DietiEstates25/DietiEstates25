import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';

@Component({
  selector: 'app-step-cadastral',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './step-cadastral.component.html',
})
export class StepCadastralComponent implements OnInit {
  private fb = inject(FormBuilder);
  private facade = inject(CreateAdFacade);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  form = this.fb.nonNullable.group({
    price: [null as number | null, [Validators.required, Validators.min(0)]],
    rooms: [null as number | null, [Validators.required, Validators.min(0)]],
    floor: [null as number | null, [Validators.required, Validators.min(0)]],
    energyClass: ['' as any, Validators.required],
    squareMeters: [
      null as number | null,
      [Validators.required, Validators.min(1)],
    ],
  });

  ngOnInit(): void {
    const c = this.facade.cadastral();
    if (c) this.form.patchValue(c, { emitEvent: false });
  }

  next() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.facade.setCadastral(this.form.getRawValue() as any);
    this.router.navigate(['../photos'], { relativeTo: this.route });
  }
}
