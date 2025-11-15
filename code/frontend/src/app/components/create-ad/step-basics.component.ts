import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade, Category } from './create-ad.facade';

@Component({
  selector: 'app-step-basics',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './step-basics.component.html',
})
export class StepBasicsComponent implements OnInit {
  private activatedRoute = inject(ActivatedRoute);
  private formBuilder = inject(FormBuilder);
  private facade = inject(CreateAdFacade);
  private routerService = inject(Router);

  form = this.formBuilder.nonNullable.group({
    category: ['SALE' as Category, Validators.required],
    description: ['', [Validators.required, Validators.minLength(3)]],
  });

  ngOnInit(): void {
    const basics = this.facade.basics();
    if (basics) this.form.patchValue(basics, { emitEvent: false });
  }

  next() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.facade.setBasics(this.form.getRawValue());
    this.routerService.navigate(['../details'], { relativeTo: this.activatedRoute });
  }
}
