import {
  Component,
  EventEmitter,
  Input,
  Output,
  inject,
  input,
} from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OfferControllerService } from '../../services/services';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../manual_services/auth/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-offer-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
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
  isIncorrectAmount = false;
  successMessage = '';
  error = '';

  form = this.formBuilder.group({
    amount: [null, [Validators.required, Validators.min(1)]],
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

    const currentAmount = this.form.controls.amount.value!;

    if (currentAmount < this.realEstatePrice()) {
      this.isIncorrectAmount = true;
      return;
    }

    this.isIncorrectAmount = false;
    this.loading = true;
    try {
      const body = {
        category: 'OFFER',
        status: 'PENDING',
        amount: Number(this.form.value.amount),
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
