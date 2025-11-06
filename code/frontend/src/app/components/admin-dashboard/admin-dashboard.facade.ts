import { Injectable, inject } from '@angular/core';
import { Observable, throwError, of } from 'rxjs';
import { map, catchError, switchMap } from 'rxjs/operators';

import {
  RealEstateControllerService,
  EstateAgentControllerService,
  AdminControllerService,
} from '../../services/services';

import { EstateAgent } from '../../services/models/estate-agent';
import { RealEstateResponse } from '../../services/models/real-estate-response';
import { PageRealEstateResponse } from '../../services/models/page-real-estate-response';

import { HttpClient } from '@angular/common/http';
import { ApiConfiguration } from '../../services/api-configuration';
import { AutentServiceService } from '../../autent.service.service';

import { StafferRequest } from '../../services/models';
import { StafferResponse } from '../../services/models';
import { AdminResponse } from '../../services/models';
import { EstateAgentResponse } from '../../services/models';

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

//gli account mappano i dto dell'authorization server
export interface AccountResponse {

  id?: number,
  email?: string,
  role?: string,
  locked?: boolean,
  active?: boolean
}

export interface AccountRequest {

  email?: string,
  password?: string,
  role?: string
}

@Injectable({ providedIn: 'root' })
export class AdminDashboardFacade {

  private resourceServerRealEstateService = inject(RealEstateControllerService);
  private resourceServerEstateAgentService = inject(EstateAgentControllerService);
  private resourceServerAdminService = inject(AdminControllerService);

  private authenticationServerAccountService = inject(AutentServiceService);

  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

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
    return (
      localStorage.getItem('userEmail')
    );
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

    return this.resourceServerRealEstateService
      .getPagedRealEstates({
        page: 0,
        size: 100,
      })
      .pipe(
        map((page: PageRealEstateResponse) => {
          const list = Array.isArray(page?.content)
            ? (page.content as RealEstateResponse[])
            : [];
          return list.map((re) => this.toAdminAd(re));
        }),
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
    return this.resourceServerRealEstateService.deleteRealEstate({ realestateid: id }).pipe(
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

  //crea un account nell'authorization server
  private authRegister(
    email: string,
    password: string,
    role?: Role
  ): Observable<AccountResponse> {

    const body: AccountRequest = {
      email: email,
      password: password,
      role: role
    };

    return this.authenticationServerAccountService.register(body)
  }

  //crea un account nel resource server
  createUser(body: {
    email: string;
    role: Role;
    password: string;
  }): Observable<AdminUser> {

    const {email, role, password} = body;

    switch (body.role) {
      case 'AGENT': {
        // crea account credenziale su authorization server
        return this.authRegister(
          email,
          password,
          'AGENT'
        )
        .pipe(
          // collega come staffer nel dominio applicativo
          switchMap(() => {
            const payload: StafferRequest = { 
              email: email
            };
            return this.resourceServerEstateAgentService.registerEstateAgent({ body: payload });
          }),
          map((agent: EstateAgentResponse) => ({
            id: Number((agent as any)?.id) || Date.now(),
            email,
            role: 'AGENT' as const,
            active: true,
          })),
          catchError((e) => {
            console.error('[AdminDashboard] createUser(AGENT) error', e);
            return throwError(() => e);
          })
        );
      }

      case 'ADMIN': {
        // crea account credenziale su authorization server
        return this.authRegister(
          email,
          password, 
          'ADMIN'
        ).pipe(
          // registra come admin nel dominio applicativo
          switchMap(() => {
            const payload: StafferRequest = { 
              email: email
            };
            return this.resourceServerAdminService.registerAdmin({ body: payload })
          }),
          map(() => ({
            id: Date.now(),
            email,
            role: 'ADMIN' as const,
            active: true,
          })),
          catchError((e) => {
            console.error('[AdminDashboard] createUser(ADMIN) error', e);
            return throwError(() => e);
          })
        );
      }

      default:
        return throwError(
          () => new Error(`Ruolo non supportato: ${role as string}`)
        );
    }
  }
}
