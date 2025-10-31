import { Injectable, inject } from '@angular/core';
import { Observable, throwError, of } from 'rxjs';
import { map, catchError, switchMap } from 'rxjs/operators';

import {
  RealEstateControllerService,
  EstateAgentAuthenticationControllerService,
  AdminAuthenticationControllerService,
} from '../../services/services';

import { StafferRequest } from '../../services/models/staffer-request';
import { EstateAgent } from '../../services/models/estate-agent';
import { RealEstateResponse } from '../../services/models/real-estate-response';

export interface AdminAd {
  id: number;
  title: string;
  city?: string | null;
  price?: number | null;
  active?: boolean | null;
  createdAt?: string | null;
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
  private estateApi = inject(RealEstateControllerService);
  private agentAuth = inject(EstateAgentAuthenticationControllerService);
  private adminAuth = inject(AdminAuthenticationControllerService);

  // ===== Util =====
  private findAnyJwtFromClientStorage(): string | null {
    const candidatesKeys = [
      'token',
      'jwt',
      'access_token',
      'id_token',
      'auth_token',
      'Authorization',
    ];
    const isJwt = (v: string | null | undefined) =>
      !!v && /^[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+$/.test(v);
    const stripBearer = (v: string) =>
      v.startsWith('Bearer ') ? v.slice('Bearer '.length).trim() : v.trim();

    for (const k of candidatesKeys) {
      const v1 = localStorage.getItem(k);
      if (v1 && isJwt(stripBearer(v1))) return stripBearer(v1);
      const v2 = sessionStorage.getItem(k);
      if (v2 && isJwt(stripBearer(v2))) return stripBearer(v2);
    }
    for (let i = 0; i < localStorage.length; i++) {
      const val = localStorage.getItem(localStorage.key(i)!);
      const s = stripBearer(val ?? '');
      if (isJwt(s)) return s;
    }
    for (let i = 0; i < sessionStorage.length; i++) {
      const val = sessionStorage.getItem(sessionStorage.key(i)!);
      const s = stripBearer(val ?? '');
      if (isJwt(s)) return s;
    }
    try {
      const cookie = document?.cookie ?? '';
      const parts = cookie.split(';').map((p) => p.split('=').pop()!.trim());
      for (const p of parts) {
        const s = stripBearer(decodeURIComponent(p));
        if (isJwt(s)) return s;
      }
    } catch {
      /* ignore */
    }
    return null;
  }

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
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      const payload = JSON.parse(jsonPayload) as Record<string, unknown>;
      const maybeEmail =
        (payload['email'] as string | undefined) ??
        (payload['preferred_username'] as string | undefined) ??
        (payload['upn'] as string | undefined) ??
        (payload['username'] as string | undefined) ??
        (typeof payload['sub'] === 'string' && payload['sub'].includes('@')
          ? (payload['sub'] as string)
          : undefined);
      return typeof maybeEmail === 'string' ? maybeEmail : null;
    } catch {
      return null;
    }
  }

  // ===== ADS =====
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

    return this.estateApi.listAllRealEstates().pipe(
      map((list) => (list ?? []).map(this.toAdminAd)),
      map((ads) => {
        const qNorm = (q ?? '').trim().toLowerCase();
        let res = ads;
        if (qNorm) {
          res = res.filter((a) =>
            [a.title, a.city, String(a.price ?? ''), String(a.id)]
              .filter(Boolean)
              .some((v) => String(v).toLowerCase().includes(qNorm))
          );
        }
        if (active !== '' && active !== undefined) {
          res = res.filter((a) => (a.active ?? null) === (active as boolean));
        }
        return res;
      }),
      catchError((e) => {
        console.error('[AdminDashboard] listAds error', e);
        return of([]);
      })
    );
  }

  updateAd(
    _: number,
    __: Partial<Pick<AdminAd, 'title' | 'price' | 'active'>>
  ): Observable<void> {
    return throwError(
      () => new Error('updateAd non supportato dagli OpenAPI services.')
    );
  }
  deleteAd(id: number): Observable<void> {
    return this.estateApi.deleteRealEstate({ realestateid: id }).pipe(
      switchMap(() => this.listAds({})),
      catchError((e) => {
        console.error('[AdminDashboard] deleteAd error', e);
        return of([]);
      }),
      map(() => void 0)
    );
  }

  private toAdminAd = (re: RealEstateResponse): AdminAd => {
    const images = re.images ?? [];
    const first = images[0] ?? null;
    return {
      id: Number(re.id ?? 0),
      title: re.description ?? `Annuncio #${re.id ?? '?'}`,
      city: null,
      price: null,
      active: null,
      createdAt: re.createdDate ?? null,
      // coverSrc: first ? `data:image/jpeg;base64,${first}` : null,
    };
  };

  // ===== USERS =====

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

    const adminEmail = this.getAdminEmailFromJwt();
    if (!adminEmail) {
      return throwError(
        () => new Error('adminEmail non reperibile dal token JWT.')
      );
    }

    switch (role) {
      case 'AGENT': {
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
}
