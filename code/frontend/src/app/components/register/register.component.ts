import { Component, inject, signal } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthenticationControllerService } from '../../services/services/authentication-controller.service';
import { AuthenticationRequest } from '../../services/models/authentication-request';

function matchPassword(group: AbstractControl): ValidationErrors | null {
  const p = group.get('password')?.value;
  const c = group.get('confirm')?.value;
  return p && c && p !== c ? { mismatch: true } : null;
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private api = inject(AuthenticationControllerService);
  private router = inject(Router);

  loading = signal(false);
  errorMsg = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    passwords: this.fb.nonNullable.group(
      {
        password: ['', Validators.required],
        confirm: ['', Validators.required],
      },
      { validators: matchPassword }
    ),
  });

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.errorMsg.set(null);

    const { name, email, passwords } = this.form.getRawValue();
    const body: AuthenticationRequest = {
      email,
      password: passwords.password,
      role: 'CLIENT',
    };

    this.api.register({ body }).subscribe({
      next: () => this.router.navigateByUrl('/auth/login'),
      error: (err) => {
        this.errorMsg.set(err?.error?.message || 'Registrazione non riuscita');
        this.loading.set(false);
      },
    });
  }
}
