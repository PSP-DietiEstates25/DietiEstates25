import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

export interface AdminAd {
  id: number;
  title: string;
  city?: string;
  price?: number | null;
  active?: boolean;
  createdAt?: string;
}
export type Role = 'ADMIN' | 'AGENT';
export interface AdminUser {
  id: number;
  name: string;
  email: string;
  role: Role;
  active: boolean;
  createdAt?: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private http = inject(HttpClient);

  // ADS
  listAds(opts?: { q?: string; active?: boolean | '' }) {
    let params = new HttpParams();
    if (opts?.q) params = params.set('q', opts.q);
    if (opts && opts.active !== '')
      params = params.set('active', String(opts.active));
    return this.http.get<AdminAd[]>('/api/admin/ads', { params });
  }
  updateAd(id: number, patch: Partial<AdminAd>) {
    return this.http.patch(`/api/admin/ads/${id}`, patch);
  }
  deleteAd(id: number) {
    return this.http.delete(`/api/admin/ads/${id}`);
  }

  // USERS
  listUsers(role?: Role | '') {
    const params = role ? new HttpParams().set('role', role) : undefined;
    return this.http.get<AdminUser[]>('/api/admin/users', { params });
  }
  createUser(body: {
    name: string;
    email: string;
    role: Role;
    password?: string;
  }) {
    return this.http.post<AdminUser>('/api/admin/users', body);
  }
  updateUser(id: number, patch: Partial<Pick<AdminUser, 'active' | 'role'>>) {
    return this.http.patch(`/api/admin/users/${id}`, patch);
  }
}
