import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import {
  AdminDashboardFacade,
  AdminAd,
  AdminUser,
  Role,
} from './admin-dashboard.facade';
import { Router, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { AuthService } from '../../manual_services/auth/auth.service';
import { environment } from '../../../environments/environment.development';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

function matchValidator(a: string, b: string) {
  return (ctrl: AbstractControl) => {
    const v1 = ctrl.get(a)?.value;
    const v2 = ctrl.get(b)?.value;
    return v1 && v2 && v1 !== v2 ? { mismatch: true } : null;
  };
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
})
export class AdminDashboardComponent {
  private facade = inject(AdminDashboardFacade);
  private toastrService = inject(ToastrService);
  private formBuilder = inject(FormBuilder);
  private routerService = inject(Router);

  private readonly authService = inject(AuthService);

  isAuthenticated = false;
  email = '';

  tabs = [
    { key: 'ads' as const, label: 'Annunci' },
    { key: 'users' as const, label: 'Utenti' },
    { key: 'passwords' as const, label: 'Password' },
  ];
  active = signal<'passwords' | 'ads' | 'users'>('ads');

  ads = signal<AdminAd[]>([]);
  adsLoading = signal(false);
  q = signal('');
  activeFilter = signal<boolean | ''>('');

  users = signal<AdminUser[]>([]);
  usersLoading = signal(false);
  roleFilter = signal<Role | ''>('');

  createForm = this.formBuilder.nonNullable.group(
    {
      email: ['', [Validators.required, Validators.email]],
      role: ['ESTATE_AGENT' as Role, [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirm: ['', [Validators.required]],
    },
    { validators: matchPassword },
  );

  constructor() {
    this.loadAds();
  }

  setTab(t: 'ads' | 'users' | 'passwords') {
    this.active.set(t);

    if (t === 'ads') {
      this.loadAds();
    }
    // else if (t === 'users') {
    //   this.createForm.reset({
    //     email: '',
    //     role: 'ESTATE_AGENT',
    //     password: '',
    //   });
    // }
  }

  loadAds() {
    this.adsLoading.set(true);
    this.facade
      .listAds({ q: this.q().trim() || undefined, active: this.activeFilter() })
      .subscribe({
        next: (list) => this.ads.set(list || []),
        error: (_) => this.ads.set([]),
        complete: () => this.adsLoading.set(false),
      });
  }

  deleteAd(ad: AdminAd) {
    if (!confirm(`Eliminare l'annuncio "${ad.title}"?`)) return;
    this.facade.deleteAd(ad.id).subscribe({ next: (_) => this.loadAds() });
  }

  createUser() {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }

    const { email, role, password } = this.createForm.getRawValue();

    this.facade.createUser({ email, role, password }).subscribe({
      next: (response) => {
        this.createForm.reset({
          email: '',
          role: 'ESTATE_AGENT',
          password: '',
          confirm: '',
        });

        if (response.role === 'ESTATE_AGENT')
          this.toastrService.success('Account agente creato', 'Account creato');
        else if (response.role === 'ADMIN')
          this.toastrService.success('Account admin creato', 'Account creato');
      },
      error: (response: HttpErrorResponse) => {
        if (response.error === 501)
          this.toastrService.error(
            'Errore interno del server',
            'Contatta un Admin',
          );
      },
    });
  }

  logout() {
    this.authService.logout().subscribe(() => {
      this.isAuthenticated = false;
      this.email = '';
      this.routerService.navigateByUrl(
        `${environment.apiBaseUrl}/oauth2/authorization/messaging-client-oidc?prompt=login`,
      );
    });
  }

  form = this.formBuilder.group(
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
    { validators: matchValidator('newPassword', 'confirmNewPassword') },
  );

  loading = this.facade.loading;
  success = this.facade.success;
  error = this.facade.error;

  get formControls() {
    return this.form.controls;
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const current = this.formControls.currentPassword.value!;
    const next = this.formControls.newPassword.value!;

    this.facade.changePassword(current, next).subscribe({
      next: () => {
        this.form.reset();
        this.form.markAsPristine();
        this.form.markAsUntouched();
      },
    });
  }
}

function matchPassword(group: AbstractControl): ValidationErrors | null {
  const p = group.get('password')?.value;
  const c = group.get('confirm')?.value;
  return p && c && p !== c ? { mismatch: true } : null;
}
