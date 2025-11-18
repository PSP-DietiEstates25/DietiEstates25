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

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, SocialLoginButtons],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private formBuilder = inject(FormBuilder);
  private cookieService = inject(CookieService);
  loginProcessingUrl!: string;

  submitted = false;
  loading = false;
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.minLength(5)]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
      Validators.maxLength(15),
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
    window.location.href = `${environment.frontendBaseUrl}/register`;
  }

  csrfToken() {
    return this.cookieService.get('XSRF-TOKEN');
  }

  onSubmit(event: Event) {
    //Prevenzione del comportamento nativo
    event.preventDefault();
    this.submitted = true;

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    const nativeForm = event.target as HTMLFormElement;
    nativeForm.submit();
  }
}
