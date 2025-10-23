import { Component, inject, signal } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { RegisterControllerService } from '../../services/authorization_server/services';
import { RegisterRequest } from '../../services/authorization_server/models';

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

  private formBuilder = inject(FormBuilder);
  private registerService = inject(RegisterControllerService);
  private router = inject(Router);

  loading = signal(false);
  errorMsg = signal<string | null>(null);

  registerForm = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    passwords: this.formBuilder.nonNullable.group(
      {
        password: ['', Validators.required],
        confirm: ['', Validators.required],
      },
      { validators: matchPassword }
    ),
  });

  submit() {
    if (this.registerForm.invalid) {
      if (this.registerForm.get('passwords')?.errors?.['mismatch']) {
        this.errorMsg.set('Le password non coincidono');
      }
      this.registerForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMsg.set(null);

    const raw = this.registerForm.getRawValue();
    const email = raw.email.trim();
    const password = raw.passwords.password.trim();
    const role = 'USER';

    const body: RegisterRequest = { email, password, role };

    this.registerService.register({ body }).subscribe({
      next: () => {
        localStorage.setItem('userEmail', email);
        this.loading.set(false);
        this.router.navigateByUrl('/');
      },
      error: (err) => {
        this.errorMsg.set(err?.error?.message || 'Registrazione non riuscita');
        this.loading.set(false);
      },
    });
  }
}
