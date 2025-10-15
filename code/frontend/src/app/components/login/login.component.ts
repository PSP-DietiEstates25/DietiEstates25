import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthenticationControllerService } from '../../services/services/authentication-controller.service';
import { AuthenticationRequest } from '../../services/models/authentication-request';

import { AuthGoogleService } from '../../services/services/auth/google/auth-google.service';

import { generate32ByteChallenge } from '../../services/services/auth/generate32ByteChallengeFn';
import { validateChallenge } from '../../services/services/auth/validateChallengeFn';

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
  private googleAuthService = inject(AuthGoogleService);

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
    this.loading.set(true);
    this.errorMsg.set(null);

    const { email, password } = this.form.getRawValue();
    const body: AuthenticationRequest = { email, password };

    this.api.login({ body }).subscribe({
      next: (res) => {
        const token = res?.token ?? '';
        localStorage.setItem('auth.token', token);
        localStorage.setItem('token', token);
        localStorage.setItem('userEmail', email);

        const claims = safeDecodeJwt(token) ?? {};
        const authorities: string[] = Array.isArray(claims.authorities)
          ? claims.authorities
          : [];

        const roleFromAuthorities = authorities.includes('ESTATE_AGENT')
          ? 'AGENT'
          : authorities.includes('ADMIN')
          ? 'ADMIN'
          : authorities.includes('CLIENT')
          ? 'CLIENT'
          : authorities.includes('USER')
          ? 'CLIENT'
          : '';

        const effectiveRole = roleFromAuthorities as
          | 'ADMIN'
          | 'AGENT'
          | 'CLIENT'
          | '';

        switch (effectiveRole) {
          case 'AGENT':
            this.router.navigateByUrl('/agent');
            break;
          case 'ADMIN':
            this.router.navigateByUrl('/admin');
            break;
          default:
            this.router.navigateByUrl('/');
        }

        this.loading.set(false);
      },
      error: (err) => {
        this.errorMsg.set(err?.error?.message || 'Credenziali non valide');
        this.loading.set(false);
      },
    });
  }

  signInWithGoogle() {
    this.googleAuthService.login();
  }
}

function safeDecodeJwt(token: string | null): any | null {
  try {
    if (!token) return null;
    const part = token.split('.')[1];
    if (!part) return null;
    let base64 = part.replace(/-/g, '+').replace(/_/g, '/');
    base64 += '='.repeat((4 - (base64.length % 4)) % 4);
    return JSON.parse(atob(base64));
  } catch {
    return null;
  }
}
