import { Component, OnDestroy, computed, effect, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { ToastrService } from 'ngx-toastr';
import { DiscardDialogComponent } from '../dialog/discard-dialog/discard-dialog.component';
import { CadastralDraft } from '../../interfaces/create-ad/cadastral-draft';

@Component({
  selector: 'app-step-cadastral',
  standalone: true,
  imports: [ReactiveFormsModule, DiscardDialogComponent],
  templateUrl: './step-cadastral.component.html',
})
export class StepCadastralComponent implements OnDestroy {
  private formBuilder = inject(FormBuilder);
  private toastrService = inject(ToastrService);
  private facade = inject(CreateAdFacade);
  private routerService = inject(Router);
  private activatedRouter = inject(ActivatedRoute);

  private savedCadastralData = computed(() => this.facade.getCadastralData());
  isDiscardModalOpen = false;

  private skipAutosave = false;

  form = this.formBuilder.nonNullable.group({
    price: [null as number | null, [Validators.required, Validators.min(0)]],
    rooms: [null as number | null, [Validators.required, Validators.min(0)]],
    floor: [null as number | null, [Validators.required, Validators.min(0)]],
    energyClass: ['' as any, Validators.required],
    squareMeters: [
      null as number | null,
      [Validators.required, Validators.min(1)],
    ],
  });

  constructor() {
    effect(() => {
      const saved: CadastralDraft | null = this.savedCadastralData();
      if (!saved) return;

      this.form.patchValue(
        {
          price: saved.price,
          rooms: saved.rooms,
          floor: saved.floor,
          energyClass: saved.energyClass as any,
          squareMeters: saved.squareMeters,
        },
        { emitEvent: false },
      );
    });
  }

  openDiscardModal() {
    this.isDiscardModalOpen = true;
  }

  closeDiscardModal() {
    this.isDiscardModalOpen = false;
  }

  confirmDiscard() {
    this.closeDiscardModal();
    this.skipAutosave = true;
    this.facade.clearSavedData();
    this.routerService.navigate(['/']);
    this.toastrService.error('Creazione annuncio interrotta!', 'Cancellazione');
  }

  previous() {
    this.saveFormData();
    this.routerService.navigate(['../details'], {
      relativeTo: this.activatedRouter,
    });
  }

  next() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saveFormData();
    this.routerService.navigate(['../photos'], {
      relativeTo: this.activatedRouter,
    });
  }

  saveFormData() {
    this.facade.setCadastral(this.form.getRawValue() as any);
  }

  ngOnDestroy() {
    if (this.skipAutosave) return;
    this.saveFormData();
  }
}
