import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OfferControllerService } from '../../services/services';
import { firstValueFrom } from 'rxjs';
@Component({
  selector: 'app-offer-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './offer-form.component.html',
})
export class OfferFormComponent {
  private fb = inject(FormBuilder);
  private offerApi = inject(OfferControllerService);
  @Input() realEstateId!: number;
  @Input() isLoggedIn = false;
  @Input() auth?: { getEmail: () => string | null };
  @Output() success = new EventEmitter<void>();
  @Output() loginRequired = new EventEmitter<void>();
  loading = false;
  ok = '';
  err = '';
  form = this.fb.group({
    amount: [null, [Validators.required, Validators.min(1)]],
  });
  async submitOffer() {
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
        category: 'OFFER',
        status: 'PENDING',
        userEmail: email,
        amount: Number(this.form.value.amount),
      };

      await firstValueFrom(
        this.offerApi.createOffer({ realestateid: this.realEstateId, body })
      );

      this.ok = 'Offerta inviata!';
      this.success.emit();
      this.form.reset();
    } catch (e: any) {
      this.err = e?.error?.message ?? 'Errore durante l’invio dell’offerta.';
    } finally {
      this.loading = false;
    }
  }
}
