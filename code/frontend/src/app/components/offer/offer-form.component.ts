import {
  Component,
  EventEmitter,
  Input,
  Output,
  inject,
  input,
} from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';

import { OfferControllerService } from '../../services/services';
import { firstValueFrom } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-offer-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './offer-form.component.html',
})
export class OfferFormComponent {
  private formBuilder = inject(FormBuilder);
  private offerService = inject(OfferControllerService);
  private toastrService = inject(ToastrService);

  @Input()
  realEstateId!: number;

  realEstatePrice = input.required<number>();

  @Input()
  isLoggedIn = false;

  @Output()
  success = new EventEmitter<void>();

  @Output()
  loginRequired = new EventEmitter<void>();

  loading = false;
  successMessage = '';
  error = '';

  form = this.formBuilder.group({
    amount: [null, [Validators.required, Validators.min(0.01)]],
  });

  async submitOffer() {
    this.successMessage = '';
    this.error = '';

    if (!this.isLoggedIn) {
      this.loginRequired.emit();
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    try {
      const amount = Number(this.form.value.amount);

      const body = {
        category: 'OFFER',
        status: 'PENDING',
        amount,
      };

      await firstValueFrom(
        this.offerService.createOffer({
          realestateid: this.realEstateId,
          body,
        }),
      );

      this.successMessage = 'Offerta inviata!';
      this.toastrService.success('Offerta inviata.', 'Successo');
      this.success.emit();
      this.form.reset();
    } catch (error: any) {
      this.toastrService.error(
        `Errore durante l'invio dell'offerta, contatta un admin.`,
        `Errore`,
      );
    } finally {
      this.loading = false;
    }
  }
}
