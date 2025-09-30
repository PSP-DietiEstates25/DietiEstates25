import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthenticationControllerService } from '../../services/services/authentication-controller.service';
import { AuthenticationRequest } from '../../services/models/authentication-request';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private api = inject(AuthenticationControllerService);
  private router = inject(Router);

  loading = signal(false);
  errorMsg = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { email, password } = this.form.getRawValue();

    this.loading.set(true);
    this.errorMsg.set(null);

    const body: AuthenticationRequest = { email, password, role: 'CLIENT' };
    this.api.login({ body }).subscribe({
      next: (res) => {
        // salva token per l’interceptor
        localStorage.setItem('auth.token', res?.token ?? '');
        // salva ruolo
        localStorage.setItem('auth.role', 'CLIENT');
        this.router.navigateByUrl('/');
      },
      error: (err) => {
        this.errorMsg.set(err?.error?.message || 'Credenziali non valide');
        this.loading.set(false);
      },
    });
  }
}
