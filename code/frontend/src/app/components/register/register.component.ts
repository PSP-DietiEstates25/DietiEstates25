import { Component, inject, signal } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
  ValidationErrors,
  FormControl,
  FormGroup,
} from '@angular/forms';
import { AuthService } from '../../manual_services/auth/auth.service';
import { environment } from '../../../environments/environment';
import { firstValueFrom } from 'rxjs';
import { AccountRequest } from '../../interfaces/account-request';
import { UserControllerService } from '../../services/services';

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
  submitted = false;
  accountAlreadyExists = false;

  registerForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.minLength(5)]),
    passwords: new FormGroup(
      {
        password: new FormControl('', [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(64),
        ]),
        confirm: new FormControl('', [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(64),
        ]),
      },
      { validators: matchPassword },
    ),
  });

  get email(){
    return this.registerForm.get('email');
  }

  get password() {
    return this.registerForm.get('passwords.password');
  }

  get confirm(){
    return this.registerForm.get('passwords.confirm');
  }

  onClickLogin(): void {
    window.location.href = environment.loginUrl;
  }

  async submit(): Promise<void> {
    this.submitted = true;

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
    const email = raw.email!.trim();
    const password = raw.passwords.password!.trim();
    const body = { email, password, role: 'USER' } as AccountRequest;

    try {
      await firstValueFrom(this.authService.getCsrf());

      const exists = await firstValueFrom(this.authService.register(body));
      
      if (exists) {
        
        this.accountAlreadyExists = true;
        return;
      }

      window.location.href = environment.loginUrl;
    } catch (err: any) {
      this.errorMsg.set(err?.error?.message || 'Registrazione non riuscita');
    } finally {
      this.loading.set(false);
    }
  }
}
