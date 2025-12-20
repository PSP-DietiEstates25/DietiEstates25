import { Component, inject, signal } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';

import { Router } from '@angular/router';
import { AuthService } from '../../manual_services/auth/auth.service';
import { environment } from '../../../environments/environment';
import { firstValueFrom } from 'rxjs';
import { AccountRequest } from '../admin-dashboard/admin-dashboard.facade';
import { UserControllerService } from '../../services/services';
import { UserRequest } from '../../services/models';
import { RegisterUser$Params } from '../../services/fn/user-controller/register-user';

function matchPassword(group: AbstractControl): ValidationErrors | null {
  const p = group.get('password')?.value;
  const c = group.get('confirm')?.value;
  return p && c && p !== c ? { mismatch: true } : null;
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  private formBuilder = inject(FormBuilder);
  private authService = inject(AuthService);
  private userService = inject(UserControllerService);

  loading = signal(false);
  errorMsg = signal<string | null>(null);

  registerForm = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    passwords: this.formBuilder.nonNullable.group(
      {
        password: ['', Validators.required],
        confirm: ['', Validators.required],
      },
      { validators: matchPassword },
    ),
  });

  onClickLogin(): void {
    window.location.href = environment.loginUrl;
  }

  async submit(): Promise<void> {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      this.errorMsg.set(
        this.registerForm.get('passwords')?.errors?.['mismatch']
          ? 'Le password non coincidono'
          : null,
      );
      return;
    }

    this.loading.set(true);
    this.errorMsg.set(null);

    const raw = this.registerForm.getRawValue();
    const email = raw.email.trim();
    const password = raw.passwords.password.trim();
    const body = { email, password, role: 'USER' } as AccountRequest;

    try {
      await firstValueFrom(this.authService.getCsrf());
      await firstValueFrom(this.authService.register(body));
      window.location.href = environment.loginUrl;
    } catch (err: any) {
      this.errorMsg.set(err?.error?.message || 'Registrazione non riuscita');
    } finally {
      this.loading.set(false);
    }
  }
}
