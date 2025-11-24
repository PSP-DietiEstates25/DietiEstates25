import {
  Component,
  OnInit,
  Signal,
  inject,
  effect,
  computed,
} from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { CadastralDataDraft } from '../edit-ad/edit-ad.facade';
import { ToastrService } from 'ngx-toastr';
import { DiscardDialogComponent } from '../dialog/discard-dialog/discard-dialog.component';

@Component({
  selector: 'app-step-cadastral',
  standalone: true,
  imports: [ReactiveFormsModule, DiscardDialogComponent],
  templateUrl: './step-cadastral.component.html',
})
export class StepCadastralComponent {
  
  private formBuilder = inject(FormBuilder);
  private toastrService = inject(ToastrService);
  private facade = inject(CreateAdFacade);
  private routerService = inject(Router);
  private activatedRouter = inject(ActivatedRoute);

  _savedCadastralData!: Signal<CadastralDataDraft | null>;
  isDiscardModalOpen = false;

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
      this._savedCadastralData = computed(() => this.facade.getCadastralData());
      if (this._savedCadastralData()?.price !== null) {
        this.form.patchValue({ price: this._savedCadastralData()?.price });
      }
      if (this._savedCadastralData()?.rooms !== null) {
        this.form.patchValue({ rooms: this._savedCadastralData()?.rooms });
      }
      if (this._savedCadastralData()?.floor !== null) {
        this.form.patchValue({ floor: this._savedCadastralData()?.floor });
      }
      if (this._savedCadastralData()?.energyClass !== null) {
        this.form.patchValue({
          energyClass: this._savedCadastralData()?.energyClass,
        });
      }
      if (this._savedCadastralData()?.squareMeters !== null) {
        this.form.patchValue({
          squareMeters: this._savedCadastralData()?.squareMeters,
        });
      }
    });
  }

  /*
  ngOnInit(): void {
    const cadastralData = this.facade.cadastralData();
    if (cadastralData) this.form.patchValue(cadastralData, { emitEvent: false });
  }
  */

  openDiscardModal() {
    this.isDiscardModalOpen = true;
  }

  closeDiscardModal() {
    this.isDiscardModalOpen = false;
  }

  confirmDiscard() {
    this.closeDiscardModal();
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
}
