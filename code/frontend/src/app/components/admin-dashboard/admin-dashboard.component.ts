import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  Validators,
} from '@angular/forms';
import {
  AdminDashboardFacade,
  AdminAd,
  AdminUser,
  Role,
} from './admin-dashboard.facade';
import { Router, RouterLink } from '@angular/router';

import { AutentServiceService } from '../../auth.service';
import { environment } from '../../../environments/environment.development';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
})
export class AdminDashboardComponent {
  private facade = inject(AdminDashboardFacade);
  private formBuilder = inject(FormBuilder);
  private routerService = inject(Router);

  private readonly autent = inject(AutentServiceService);

  isAuthenticated = false;
  email = '';

  tabs = [
    { key: 'ads' as const, label: 'Annunci' },
    { key: 'users' as const, label: 'Utenti' },
  ];
  active = signal<'ads' | 'users'>('ads');

  ads = signal<AdminAd[]>([]);
  adsLoading = signal(false);
  q = signal('');
  activeFilter = signal<boolean | ''>('');
  editId = signal<number | null>(null);
  editTitle = signal('');
  editPrice = signal<number | null>(null);
  editActive = signal<boolean>(true);

  users = signal<AdminUser[]>([]);
  usersLoading = signal(false);
  roleFilter = signal<Role | ''>('');

  createForm = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    role: ['ESTATE_AGENT' as Role, [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  constructor() {
    this.loadAds();
  }

  setTab(t: 'ads' | 'users') {
    this.active.set(t);
    if (t === 'ads') this.loadAds();
    else this.createUser();
  }

  loadAds() {
    this.adsLoading.set(true);
    this.facade.listAds({ q: this.q().trim() || undefined, active: this.activeFilter() })
      .subscribe({
        next: (list) => this.ads.set(list || []),
        error: (_) => this.ads.set([]),
        complete: () => this.adsLoading.set(false),
      });
  }

  startEdit(a: AdminAd) {
    this.editId.set(a.id);
    this.editTitle.set(a.title || '');
    this.editPrice.set(a.price ?? null);
    this.editActive.set(!!a.active);
  }

  cancelEdit() {
    this.editId.set(null);
  }

  saveEdit() {
    const id = this.editId();
    if (!id) return;
    this.facade.updateAd(id, {
        title: this.editTitle(),
        price: this.editPrice(),
        active: this.editActive(),
      })
      .subscribe({
        next: (_) => {
          this.cancelEdit();
          this.loadAds();
        },
      });
  }

  deleteAd(a: AdminAd) {
    if (!confirm(`Eliminare l'annuncio "${a.title}"?`)) return;
    this.facade.deleteAd(a.id).subscribe({ next: (_) => this.loadAds() });
  }

  createUser() {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }
    this.facade.createUser(this.createForm.getRawValue()).subscribe({
      next: (_) => {
        this.createForm.reset({
          email: '',
          role: 'ESTATE_AGENT',
          password: '',
        });
      },
    });
  }

  logout() {
    this.autent.logout().subscribe(() => {
      this.isAuthenticated = false;
      this.email = '';
      this.routerService.navigateByUrl(
        `${environment.apiBaseUrl}/oauth2/authorization/messaging-client-oidc?prompt=login`
      );
    });
  }
}
