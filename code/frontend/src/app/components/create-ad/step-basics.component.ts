import {
  Component,
  OnInit,
  inject,
  signal,
  effect,
  Signal,
  computed,
} from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade, Category, BasicsDraft } from './create-ad.facade';
import { ToastrService } from 'ngx-toastr';
import { DiscardDialogComponent } from '../dialog/discard-dialog/discard-dialog.component';

@Component({
  selector: 'app-step-basics',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    DiscardDialogComponent,
    DiscardDialogComponent,
  ],
  templateUrl: './step-basics.component.html',
})
export class StepBasicsComponent {
  private activatedRoute = inject(ActivatedRoute);
  private toastrService = inject(ToastrService);
  private formBuilder = inject(FormBuilder);
  private facade = inject(CreateAdFacade);
  private routerService = inject(Router);

  _savedBasics!: Signal<BasicsDraft | null>;
  isDiscardModalOpen = false;

  form = this.formBuilder.nonNullable.group({
    category: ['SALE' as Category, Validators.required],
    description: ['', [Validators.required, Validators.minLength(3)]],
  });

  constructor() {
    effect(() => {
      this._savedBasics = computed(() => this.facade.getBasics());
      if (this._savedBasics()?.category !== null) {
        this.form.patchValue({
          category: this._savedBasics()?.category,
        });
      }
      if (this._savedBasics()?.description !== null) {
        this.form.patchValue({
          description: this._savedBasics()?.description,
        });
      }
    });
  }

  /*
  ngOnInit(): void {
    const basics = this.facade.basics();
    if (basics) this.form.patchValue(basics, { emitEvent: false });
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

  next() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.facade.setBasics(this.form.getRawValue());

    this.routerService.navigate(['../details'], {
      relativeTo: this.activatedRoute,
    });
  }
}
