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

  // LOGIN reale
  async onLogin(): Promise<void> {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { email, password } = this.loginForm.value;
    this.loading = true;
    this.errorMsg = '';

    try {
      const res = await firstValueFrom(this.authService.login(email, password));
      const token = res.token ?? '';
      if (!token) {
        this.errorMsg = 'Token mancante nella risposta.';
        this.loading = false;
        return;
      }
      // Decodifica JWT: il backend mette "authorities" nel payload
      const payload: any = jwtDecode(token);
      const authorities: string[] = payload?.authorities ?? [];
      const role = String(authorities[0] ?? '')
        .replace(/^ROLE_/i, '')
        .toLowerCase();
      if (role === 'estate_agent' || role === 'agent') {
        this.router.navigate(['/agent']);
      } else if (role === 'admin') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/']);
      }
    } catch (err) {
      this.errorMsg = 'Credenziali non valide o errore di connessione.';
    } finally {
      this.loading = false;
    }
  }

  // REGISTRAZIONE reale
  async onRegister(): Promise<void> {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const { email, password } = this.registerForm.value;
    this.loading = true;
    this.errorMsg = '';

    try {
      await this.authService.register(email, password);
      alert('Registrazione avvenuta con successo! Ora puoi fare il login.');
      // opzionale: auto-login
      // await this.onLogin();
    } catch (err) {
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
