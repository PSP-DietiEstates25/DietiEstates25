import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { AdService } from '../../vecchioService/rest-backend/ad/ad.service';

@Component({
  selector: 'app-visit-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: `./visit-form.component.html`,
})
export class VisitFormComponent {
  private fb = inject(FormBuilder);
  private api = inject(AdService);

  @Input({ required: true }) adId!: number;
  @Input() isLoggedIn = false;

  @Output() loginRequired = new EventEmitter<void>();
  @Output() success = new EventEmitter<void>();

  loading = false;
  ok: string | null = null;
  err: string | null = null;

  form = this.fb.nonNullable.group({
    date: ['', Validators.required],
    time: [''],
  });

  submit() {
    if (!this.isLoggedIn) {
      this.loginRequired.emit();
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.ok = null;
    this.err = null;
    const { date, time } = this.form.getRawValue();
    this.api
      .requestVisit(this.adId, {
        date,
        time: time || undefined,
      })
      .subscribe({
        next: () => {
          this.ok = 'Richiesta inviata';
          this.form.reset();
          this.success.emit();
        },
        error: (e) => {
          this.err = e?.error?.message || 'Errore richiesta visita';
        },
        complete: () => (this.loading = false),
      });
  }
}
