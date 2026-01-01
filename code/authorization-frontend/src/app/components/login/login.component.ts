import { Component, inject, signal } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
  FormControl,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { environment } from '../../../environments/environment';
import { SocialLoginButtons } from '../social-login-buttons/social-login-buttons';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../../_services/auth/auth.service';
import { HttpErrorResponse, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, SocialLoginButtons],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private formBuilder = inject(FormBuilder);
  private cookieService = inject(CookieService);
  private authService = inject(AuthService);
  loginProcessingUrl!: string;

  submitted = false;
  badCredentialsError = false;
  loading = false;

  loginForm = new FormGroup({
    email: new FormControl('' as string, [
      Validators.required,
      Validators.minLength(5),
    ]),
    password: new FormControl('' as string, [
      Validators.required,
      Validators.minLength(5),
      Validators.maxLength(64),
    ]),
  });

  constructor() {
    this.loginProcessingUrl = environment.loginProcessingUrl;
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  goToRegister(): void {
    window.location.href = `${environment.frontendBaseUrl}/auth/register`;
  }

  csrfToken() {
    return this.cookieService.get('XSRF-TOKEN');
  }

  onSubmit(event: Event) {
    event.preventDefault();
    this.submitted = true;
    this.badCredentialsError = false;

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;

    const body = new HttpParams()
      .set('username', this.email?.value!)
      .set('password', this.password?.value!)
      .set('_csrf', this.csrfToken());

    this.authService.login(body).subscribe({
      next: (res) => {
        this.loading = false;
        window.location.href = res.redirectUrl;
      },
      error: (response: HttpErrorResponse) => {
        this.loading = false;

        const body = response.error as any;
        if (response.status === 401 && body.businessErrorCode === 1401) {
          this.badCredentialsError = true;
          return;
        }
      },
    });
  }
}
