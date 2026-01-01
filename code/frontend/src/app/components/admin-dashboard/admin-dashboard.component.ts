import { Component, effect, inject, signal } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { AdminDashboardFacade } from './admin-dashboard.facade';
import { Router, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { AuthService } from '../../manual_services/auth/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { AdsPaginatorComponent } from '../ads-paginator/ads-paginator.component';
import { AdsPaginatorService } from '../../manual_services/ads_paginator/ads-paginator.service';
import { FullRealEstate } from '../../interfaces/full-real-estate';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { AdminAdsListComponent } from '../admin-ads-list/admin-ads-list.component';
import { LocalStorageService } from '../../manual_services/local-storage/local-storage.service';
import { Role } from '../../interfaces/role';
import { AdminUser } from '../../interfaces/admin-user';
import { firstValueFrom } from 'rxjs';

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
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    AdsPaginatorComponent,
    AdminAdsListComponent,
  ],
  templateUrl: './admin-dashboard.component.html',
})
export class AdminDashboardComponent {
  facade = inject(AdminDashboardFacade);
  adsPaginatorService = inject(AdsPaginatorService);
  formBuilder = inject(FormBuilder);
  routerService = inject(Router);
  localStorageService = inject(LocalStorageService);
  toastrService = inject(ToastrService);

  private readonly authService = inject(AuthService);

  isAuthenticated = false;
  email = '';

  realEstates = this.facade.realEstates;

  adsPaginatorRequest!: PaginatorRequest;

  totalPages!: number;
  page!: number;

  tabs = [
    { key: 'ads' as const, label: 'Annunci' },
    { key: 'users' as const, label: 'Utenti' },
    { key: 'passwords' as const, label: 'Password' },
  ];
  active = signal<'passwords' | 'ads' | 'users'>('ads');

  adsLoading = signal(false);

  users = signal<AdminUser[]>([]);
  usersLoading = signal(false);
  roleFilter = signal<Role | ''>('');
  accountAlreadyExists = false;
  submitted = false;

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
    effect(() => {
      this.adsPaginatorRequest = this.adsPaginatorService.adsRequest();
      if (this.active() === 'ads') {
        this.fetchAdminRealEstates();
      }
    });
  }

  setTab(t: 'ads' | 'users' | 'passwords') {
    this.active.set(t);
    if (t === 'ads' && this.realEstates().length) {
      this.fetchAdminRealEstates();
    }
  }

  fetchAdminRealEstates() {
    this.facade.fetchRealEstates(this.adsPaginatorRequest).subscribe({
      next: (results) => {
        this.totalPages = results.totalPages!;
        this.initPages();
      },
      error: (response: HttpErrorResponse) => {
        if (response.error === 500) {
          this.toastrService.error('Contatta un admin', 'Errore interno');
          this.routerService.navigateByUrl('/');
        }
      },
    });
  }

  async createStaffer() {
    this.submitted = true;
    this.accountAlreadyExists = false;

    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      this.submitted = false;
      return;
    }

    const { email, role, password } = this.createForm.getRawValue();
    
    const body = {
      email: email,
      role: role,
      password: password
    };

    try {
      await firstValueFrom(this.authService.getCsrf());
      const created = await firstValueFrom(this.authService.register(body));
      if (!created) {
        this.accountAlreadyExists = true;
        return;
      }
    } catch (err: any) {
      console.log(err);
    } finally {
        this.loading.set(false);
    }

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

  deleteAd(adId: number) {
    this.facade.deleteAd(adId).subscribe();
  }
  
  logout(): void {
    this.authService.logoutAndRedirectToLogin();
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

  initPages() {
    this.adsPaginatorService.setPagesNumber(this.totalPages);
    this.page = this.adsPaginatorService.page();
  }

  ngOnDestroy(): void {
    this.adsPaginatorService.refresh();
  }
}

function matchPassword(group: AbstractControl): ValidationErrors | null {
  const p = group.get('password')?.value;
  const c = group.get('confirm')?.value;
  return p && c && p !== c ? { mismatch: true } : null;
}
