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
import { AccountService } from '../../_services/account/account.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, SocialLoginButtons],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private formBuilder = inject(FormBuilder);
  private cookieService = inject(CookieService);
  private accountService = inject(AccountService);
  private toastrService = inject(ToastrService);
  loginProcessingUrl!: string;

  submitted = false;
  accountDoesntExists = false;
  loading = false;
  loginForm = new FormGroup({
    email: new FormControl('' as string, [Validators.required, Validators.minLength(5)]),
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
    //Prevenzione del comportamento nativo
    event.preventDefault();
    this.submitted = true;

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    
    this.accountService.checkAccountExists({ email: this.email?.value!}).subscribe({
      next: () => {
        this.loading = true;
        const nativeForm = event.target as HTMLFormElement;
        nativeForm.submit();
        this.accountDoesntExists = false;
      },
      error: (response) => {
        if(response.businessErrorCode === 1401) {
          this.accountDoesntExists = true;
        }
      }
    });
  }
}
