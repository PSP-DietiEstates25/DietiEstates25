import { Component, inject } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AdminAccountFacade } from './admin-account.facade';

function matchValidator(a: string, b: string) {
  return (ctrl: AbstractControl) => {
    const v1 = ctrl.get(a)?.value;
    const v2 = ctrl.get(b)?.value;
    return v1 && v2 && v1 !== v2 ? { mismatch: true } : null;
  };
}

@Component({
  selector: 'app-admin-change-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './admin-change-password.component.html',
})
export class AdminChangePasswordComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  facade = inject(AdminAccountFacade);

  form = this.fb.group(
    {
      currentPassword: ['', [Validators.required]],
      newPassword: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/),
        ],
      ],
      confirmNewPassword: ['', [Validators.required]],
    },
    { validators: matchValidator('newPassword', 'confirmNewPassword') }
  );

  loading = this.facade.loading;
  ok = this.facade.ok;
  err = this.facade.err;

  get f() {
    return this.form.controls;
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const current = this.f.currentPassword.value!;
    const next = this.f.newPassword.value!;

    this.facade.changePassword(current, next).subscribe(() => {
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
      this.router.navigate(['/auth']);
    });
  }
}
