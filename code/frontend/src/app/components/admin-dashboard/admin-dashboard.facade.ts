import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { EstateAgentAuthenticationControllerService } from '../../services/services/estate-agent-authentication-controller.service';
import { StafferRequest } from '../../services/models/staffer-request';
import { EstateAgent } from '../../services/models/estate-agent';
import { AdminAuthenticationControllerService } from '../../services/services';

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
  email: string;
  role: Role;
  active: boolean;
  createdAt?: string;
}

export interface ListAdsOpts {
  q?: string;
  active?: boolean | '';
}

@Injectable({ providedIn: 'root' })
export class AdminDashboardFacade {
  private http = inject(HttpClient);
  private agentAuth = inject(EstateAgentAuthenticationControllerService);
  private adminAuth = inject(AdminAuthenticationControllerService);

private findAnyJwtFromClientStorage(): string | null {
  const candidatesKeys = [
    'token', 'jwt', 'access_token', 'id_token', 'auth_token', 'Authorization'
  ];

  const isJwt = (v: string | null | undefined) =>
    !!v && /^[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+$/.test(v);

  const stripBearer = (v: string) =>
    v.startsWith('Bearer ') ? v.slice('Bearer '.length).trim() : v.trim();

  // 1) chiavi note
  for (const k of candidatesKeys) {
    const v1 = localStorage.getItem(k);
    if (v1 && isJwt(stripBearer(v1))) return stripBearer(v1);

    const v2 = sessionStorage.getItem(k);
    if (v2 && isJwt(stripBearer(v2))) return stripBearer(v2);
  }

  // 2) scorri TUTTE le chiavi
  for (let i = 0; i < localStorage.length; i++) {
    const key = localStorage.key(i)!;
    const val = localStorage.getItem(key)!;
    const s = stripBearer(val);
    if (isJwt(s)) return s;
  }
  for (let i = 0; i < sessionStorage.length; i++) {
    const key = sessionStorage.key(i)!;
    const val = sessionStorage.getItem(key)!;
    const s = stripBearer(val);
    if (isJwt(s)) return s;
  }

  // 3) cookie: cerca qualcosa che sembri JWT
  try {
    const cookie = document?.cookie ?? '';
    const parts = cookie.split(';').map(p => p.split('=').pop()!.trim());
    for (const p of parts) {
      const s = stripBearer(decodeURIComponent(p));
      if (isJwt(s)) return s;
    }
  } catch { /* ignore */ }

  return null;
}

/** Decodifica il JWT trovato e prova a estrarre un’email utilizzabile come adminEmail */
private getAdminEmailFromJwt(): string | null {
  const token = this.findAnyJwtFromClientStorage();
  if (!token) return null;

  try {
    const base64Url = token.split('.')[1];
    if (!base64Url) return null;

    let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    base64 += '='.repeat((4 - (base64.length % 4)) % 4);

    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );

    const payload = JSON.parse(jsonPayload) as Record<string, unknown>;

    const maybeEmail =
      (payload['email'] as string | undefined) ??
      (payload['preferred_username'] as string | undefined) ??
      (payload['upn'] as string | undefined) ??
      (payload['username'] as string | undefined) ??

      ((typeof payload['sub'] === 'string' && payload['sub'].includes('@'))
        ? (payload['sub'] as string)
        : undefined);

    if (maybeEmail && typeof maybeEmail === 'string') return maybeEmail;

    console.warn('[AdminDashboard] JWT trovato ma senza email usabile. Payload:', payload);
    return null;
  } catch (e) {
    console.warn('[AdminDashboard] Decode JWT fallito:', e);
    return null;
  }
}

  // ADS
  listAds(
    arg1?: string | ListAdsOpts,
    activeParam?: boolean | ''
  ): Observable<AdminAd[]> {
    let q: string | undefined;
    let active: boolean | '' | undefined;

    if (typeof arg1 === 'string' || arg1 === undefined) {
      q = arg1;
      active = activeParam;
    } else {
      q = arg1.q;
      active = arg1.active;
    }

    let params = new HttpParams();
    if (q && q.trim()) params = params.set('q', q.trim());
    if (active !== '' && active !== undefined)
      params = params.set('active', String(active));

    return this.http.get<AdminAd[]>('/api/admin/ads', { params });
  }

  updateAd(
    id: number,
    patch: Partial<Pick<AdminAd, 'title' | 'price' | 'active'>>
  ): Observable<void> {
    return this.http.patch<void>(`/api/admin/ads/${id}`, patch);
  }

  deleteAd(id: number): Observable<void> {
    return this.http.delete<void>(`/api/admin/ads/${id}`);
  }

  // USERS
  listUsers(role?: Role | ''): Observable<AdminUser[]> {
    let params = new HttpParams();
    if (role) params = params.set('role', role);
    return this.http.get<AdminUser[]>('/api/admin/users', { params });
  }

  createUser(body: {
    email: string;
    role: Role;
    password: string;
  }): Observable<AdminUser> {
    const { email, role, password } = body;

    if (!password) {
      return throwError(
        () => new Error('La password è obbligatoria per la creazione utente.')
      );
    }

    switch (role) {
      case 'AGENT': {
        const adminEmail = this.getAdminEmailFromJwt();
        if (!adminEmail) {
          return throwError(
            () => new Error('adminEmail non reperibile dal token.')
          );
        }
        const payload: StafferRequest = { adminEmail, email, password };
        return this.agentAuth.registerEstateAgent({ body: payload }).pipe(
          map((agent: EstateAgent) => ({
            id: Number((agent as any).id) || Date.now(),
            email,
            role: 'AGENT' as const,
            active: true,
          }))
        );
      }

      case 'ADMIN': {
        const adminEmail = this.getAdminEmailFromJwt();
        if (!adminEmail) {
          return throwError(
            () => new Error('adminEmail non reperibile dal token.')
          );
        }
        return this.adminAuth
          .registerAdmin({ body: { adminEmail, email, password } })
          .pipe(
            map(() => ({
              id: Date.now(),
              email,
              role: 'ADMIN' as const,
              active: true,
            }))
          );
      }
      default:
        return throwError(
          () => new Error(`Ruolo non supportato: ${role as string}`)
        );
    }
  }

  updateUser(
    id: number,
    patch: Partial<Pick<AdminUser, 'active' | 'role'>>
  ): Observable<void> {
    return this.http.patch<void>(`/api/admin/users/${id}`, patch);
  }
}
