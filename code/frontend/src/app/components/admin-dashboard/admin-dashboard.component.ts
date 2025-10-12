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
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './admin-dashboard.component.html',
})
export class AdminDashboardComponent {
  private api = inject(AdminDashboardFacade);
  private fb = inject(FormBuilder);
  private router = inject(Router);

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

  createForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    role: ['AGENT' as Role, [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  constructor() {
    this.loadAds();
  }

  setTab(t: 'ads' | 'users') {
    this.active.set(t);
    if (t === 'ads') this.loadAds();
    else this.loadUsers();
  }

  loadAds() {
    this.adsLoading.set(true);
    this.api
      .listAds({ q: this.q().trim() || undefined, active: this.activeFilter() })
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
    this.api
      .updateAd(id, {
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
    this.api.deleteAd(a.id).subscribe({ next: (_) => this.loadAds() });
  }

  loadUsers() {
    this.usersLoading.set(true);
    this.api.listUsers(this.roleFilter() || undefined).subscribe({
      next: (list) => this.users.set(list || []),
      error: (_) => this.users.set([]),
      complete: () => this.usersLoading.set(false),
    });
  }
  createUser() {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }
    this.api.createUser(this.createForm.getRawValue()).subscribe({
      next: (_) => {
        this.createForm.reset({
          email: '',
          role: 'AGENT',
          password: '',
        });
        this.loadUsers();
      },
    });
  }
  toggleUser(u: AdminUser) {
    this.api
      .updateUser(u.id, { active: !u.active })
      .subscribe({ next: (_) => this.loadUsers() });
  }
  changeRole(u: AdminUser, role: Role) {
    if (u.role === role) return;
    this.api
      .updateUser(u.id, { role })
      .subscribe({ next: (_) => this.loadUsers() });
  }

  logout() {
    try {
      localStorage.removeItem('auth.token');
      localStorage.removeItem('token');
      localStorage.removeItem('userEmail');
      sessionStorage.removeItem('auth.token');
      sessionStorage.removeItem('token');
    } finally {
      this.router.navigateByUrl('auth/login');
    }
  }
}

function clearStorage() {
  localStorage.removeItem('userEmail');
  localStorage.removeItem('userRole');
  localStorage.removeItem('isAuthenticated');
}
