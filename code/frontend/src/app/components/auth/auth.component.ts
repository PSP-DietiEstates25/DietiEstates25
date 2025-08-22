import { Component, OnInit, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { CommonModule } from '@angular/common';
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

// Material Modules
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDividerModule } from '@angular/material/divider';

import { AuthenticationControllerService } from '../../services/services/authentication-controller.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    // Angular Material
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatTabsModule,
    MatDividerModule,
  ],

  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
})
export class AuthComponent implements OnInit {
  loginForm!: FormGroup;
  registerForm!: FormGroup;

  hideLoginPassword = true;
  hideRegisterPassword = true;
  hideRegisterConfirm = true;

  loading = false;
  errorMsg = '';

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthenticationControllerService);

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    this.registerForm = this.fb.group(
      {
        username: ['', [Validators.required, Validators.minLength(3)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', [Validators.required]],
      },
      { validators: this.passwordMatchValidator('password', 'confirmPassword') }
    );
  }

  private passwordMatchValidator(passKey: string, confirmKey: string) {
    return (group: FormGroup) => {
      const pass = group.controls[passKey];
      const confirm = group.controls[confirmKey];
      if (pass.value !== confirm.value) {
        confirm.setErrors({ notMatching: true });
      } else {
        confirm.setErrors(null);
      }
    };
  }

  // LOGIN
  async onLogin(): Promise<void> {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { email, password } = this.loginForm.value as {
      email: string;
      password: string;
    };
    this.loading = true;
    this.errorMsg = '';

    try {
      const res = await firstValueFrom(
        this.authService.login({ body: { email, password } })
      );

      const token = res.token ?? '';
      if (!token) {
        this.errorMsg = 'Token mancante nella risposta.';
        return;
      }

      localStorage.setItem('jwt', token);

      const payload: any = jwtDecode(token);
      const authorities: string[] =
        payload?.authorities ??
        payload?.roles ??
        (payload?.scope ? String(payload.scope).split(' ') : []);
      const role = String(authorities[0] ?? '')
        .replace(/^ROLE_/i, '')
        .toLowerCase();

      if (role === 'agent') this.router.navigate(['/agent']);
      else if (role === 'admin') this.router.navigate(['/admin']);
      else this.router.navigate(['/']);
    } catch (e: any) {
      this.errorMsg =
        e?.status === 401
          ? 'Credenziali non valide.'
          : 'Errore di connessione.';
    } finally {
      this.loading = false;
    }
  }

  // REGISTER
  async onRegister(): Promise<void> {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const { username, email, password } = this.registerForm.value as {
      username: string;
      email: string;
      password: string;
    };
    this.loading = true;
    this.errorMsg = '';

    try {
      await firstValueFrom(
        this.authService.register({ body: { email, password } })
      );
      alert('Registrazione avvenuta con successo! Ora puoi fare il login.');
    } catch (e) {
      this.errorMsg = 'Errore durante la registrazione.';
    } finally {
      this.loading = false;
    }
  }

  get lf() {
    return this.loginForm.controls;
  }
  get rf() {
    return this.registerForm.controls;
  }
}
