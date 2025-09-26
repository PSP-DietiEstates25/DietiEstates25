import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { AdService } from '../../vecchioService/rest-backend/ad/ad.service';

@Component({
  selector: 'app-offer-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './offer-form.component.html',
})
export class OfferFormComponent {
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
    amount: [null as number | null, [Validators.required, Validators.min(1)]],
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
    const { amount } = this.form.getRawValue();
    this.api
      .makeOffer(this.adId, {
        amount: Number(amount),
      })
      .subscribe({
        next: () => {
          this.ok = 'Offerta inviata';
          this.form.reset();
          this.success.emit();
        },
        error: (e) => {
          this.err = e?.error?.message || 'Errore invio offerta';
        },
        complete: () => (this.loading = false),
      });
  }
}
