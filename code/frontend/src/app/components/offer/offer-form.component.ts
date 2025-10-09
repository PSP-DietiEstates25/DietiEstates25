import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { OfferControllerService } from '../../services/services/offer-controller.service';
import { OfferRequest } from '../../services/models/offer-request';

@Component({
  selector: 'app-offer-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './offer-form.component.html',
})
export class OfferFormComponent {
  private fb = inject(FormBuilder);
  private api = inject(OfferControllerService);

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
    if (this.form.invalid) return;

    this.loading = true;
    this.ok = this.err = null;

    const body: OfferRequest = {
      amount: this.form.value.amount!,
      category: 'SALE',
      status: 'PENDING',
      userEmail: 'guest@public.local',
    };

    this.api.createOffer({ realestateid: this.adId, body }).subscribe({
      next: () => (this.ok = 'Offerta inviata!'),
      error: () => (this.err = 'Errore durante l’invio dell’offerta.'),
      complete: () => {
        this.loading = false;
        if (this.ok) this.success.emit();
      },
    });
  }
}
