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
    // se vuoi far scegliere il ruolo dal form, scommenta qui e nel template
    // role: this.fb.nonNullable.control<'ADMIN' | 'AGENT' | 'CLIENT'>('CLIENT'),
  });

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.errorMsg.set(null);

    const { email, password } = this.form.getRawValue();

    // imposta qui il ruolo desiderato per l’agente
    const role: 'ADMIN' | 'AGENT' | 'CLIENT' = 'AGENT';

    const body: AuthenticationRequest = { email, password, role };

    this.api.login({ body }).subscribe({
      next: (res) => {
        const token = res?.token ?? '';
        localStorage.setItem('auth.token', token);

        // prova a leggere il ruolo dal token; se manca, usa quello inviato
        const jwtRole = (
          decodeJwt(token)?.role as string | undefined
        )?.toUpperCase();
        const effectiveRole = (jwtRole as any) || role;
        localStorage.setItem('auth.role', effectiveRole);

        // redirect in base al ruolo
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
      },
      error: (err) => {
        this.errorMsg.set(err?.error?.message || 'Credenziali non valide');
        this.loading.set(false);
      },
    });
  }
}

/** Decodifica veloce del payload JWT (senza verifica) */
function decodeJwt(token: string | null): any | null {
  try {
    if (!token) return null;
    const base = token.split('.')[1];
    const json = atob(base.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(json);
  } catch {
    return null;
  }
}
