import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';

import { VisitControllerService } from '../../services/services';
import { firstValueFrom } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-visit-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './visit-form.component.html',
})
export class VisitFormComponent {
  private formBuilder = inject(FormBuilder);
  private toastrService = inject(ToastrService);
  private visitService = inject(VisitControllerService);

  @Input()
  realEstateId!: number;

  @Input()
  isLoggedIn = false;

  @Output()
  success = new EventEmitter<void>();

  @Output()
  loginRequired = new EventEmitter<void>();

  loading = false;
  successMessage = '';
  errorMessage = '';
  isDateIncorrect = false;
  today = new Date().toISOString().slice(0, 10);

  form = this.formBuilder.group({
    date: [null, [Validators.required]],
    time: [null, [Validators.required]],
  });

  async submitVisit() {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.isLoggedIn) {
      this.loginRequired.emit();
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const currentDate = new Date(Date.now());
    const selectedDate = this.form.controls.date.value;
    const selectedTime = this.form.controls.time.value;
    const selectedDateTime = new Date(`${selectedDate}T${selectedTime}`);

    if (selectedDateTime.getTime() < currentDate.getTime()) {
      this.isDateIncorrect = true;
      return;
    }

    this.isDateIncorrect = false;
    this.loading = true;
    try {
      const body = {
        category: 'VISIT',
        status: 'PENDING',
        date: this.form.value.date!, // "YYYY-MM-DD"
        time: this.form.value.time!, // "HH:mm"
      };

      await firstValueFrom(
        this.visitService.createVisit({
          realestateid: this.realEstateId,
          body,
        }),
      );

      this.toastrService.success(
        `La visita è stata prenotata con successo`,
        `Successo`,
      );
      this.success.emit();
      this.form.reset();
    } catch (error: any) {
      this.toastrService.error(
        `Errore durante l'invio della visita, contatta un admin.`,
        `Errore`,
      );
    } finally {
      this.loading = false;
    }
  }
}
