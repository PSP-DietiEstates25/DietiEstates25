import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VisitControllerService } from '../../services/services';
import { firstValueFrom } from 'rxjs';
@Component({
  selector: 'app-visit-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './visit-form.component.html',
})
export class VisitFormComponent {
  private fb = inject(FormBuilder);
  private visitApi = inject(VisitControllerService);
  @Input() realEstateId!: number;
  @Input() isLoggedIn = false;
  @Input() auth?: { getEmail: () => string | null };
  @Output() success = new EventEmitter<void>();
  @Output() loginRequired = new EventEmitter<void>();
  loading = false;
  ok = '';
  err = '';
  today = new Date().toISOString().slice(0, 10);
  form = this.fb.group({
    date: [null, [Validators.required]],
    time: [null, [Validators.required]],
  });

  async submitVisit() {
    this.ok = '';
    this.err = '';

    if (!this.isLoggedIn) {
      this.loginRequired.emit();
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const email = this.auth?.getEmail();
    if (!email) {
      this.err = 'Non riesco a leggere la tua email. Rifai login.';
      return;
    }

    this.loading = true;
    try {
      const body = {
        category: 'VISIT',
        status: 'PENDING',
        userEmail: email,
        date: this.form.value.date!, // "YYYY-MM-DD"
        time: this.form.value.time!, // "HH:mm"
      };

      await firstValueFrom(
        this.visitApi.createVisit({ realestateid: this.realEstateId, body })
      );

      this.ok = 'Richiesta inviata!';
      this.success.emit();
      this.form.reset();
    } catch (e: any) {
      this.err = e?.error?.message ?? 'Errore durante la richiesta di visita.';
    } finally {
      this.loading = false;
    }
  }
}
