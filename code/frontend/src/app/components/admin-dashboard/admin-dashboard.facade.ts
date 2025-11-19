import { Injectable, inject, signal } from '@angular/core';
import { Observable, throwError, of, EMPTY } from 'rxjs';
import { map, catchError, switchMap, tap, finalize } from 'rxjs/operators';

import {
  RealEstateControllerService,
  EstateAgentControllerService,
  AdminControllerService,
} from '../../services/services';

import { GetRealEstates$Params } from '../../services/fn/real-estate-controller/get-real-estates';
import { RealEstateResponse } from '../../services/models/real-estate-response';
import { PageRealEstateResponse } from '../../services/models/page-real-estate-response';

import { AuthService } from '../../services/auth.service';

import { StafferRequest, StafferResponse } from '../../services/models';

export interface AdminAd {
  id: number;
  title: string;
  city?: string | null;
  price?: number | null;
  active?: boolean | null;
  createdAt?: string | null;
}

export type Role = 'ADMIN' | 'ESTATE_AGENT';

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

// account dell'authorization server
export interface AccountResponse {
  id?: number;
  email?: string;
  role?: string;
  locked?: boolean;
  active?: boolean;
}

export interface AccountRequest {
  email?: string;
  password?: string;
  role?: string;
}

@Injectable({ providedIn: 'root' })
export class AdminDashboardFacade {
  private realEstateService = inject(RealEstateControllerService);
  private estateAgentService = inject(EstateAgentControllerService);
  private adminService = inject(AdminControllerService);
  private authService = inject(AuthService);

  loading = signal(false);
  success = signal<string | null>(null);
  error = signal<string | null>(null);

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

    const realEstateParams: GetRealEstates$Params = {
      page: 0,
      size: 0,
    };

    return this.realEstateService.getRealEstates(realEstateParams).pipe(
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
    return this.realEstateService.deleteRealEstate({ realestateid: id }).pipe(
      switchMap(() => this.listAds({})),
      catchError((e) => {
        console.error('[AdminDashboard] deleteAd error', e);
        return of([]);
      }),
      map(() => void 0)
    );
  }

  private toAdminAd = (re: RealEstateResponse): AdminAd => ({
    id: Number(re.id ?? 0),
    title: re.description ?? `Annuncio #${re.id ?? '?'}`,
    city: null,
    price: null,
    active: null,
    createdAt: re.createdDate ?? null,
  });

  // ===== USERS =====

  // crea un account nell'authorization server
  private authRegister(
    email: string,
    password: string,
    role?: Role
  ): Observable<AccountResponse> {
    const body: AccountRequest = { email, password, role };
    return this.authService.register(body);
  }

  // crea un account nel resource server
  createUser(body: {
    email: string;
    role: Role;
    password: string;
  }): Observable<AdminUser> {
    const { email, role, password } = body;

    switch (role) {
      case 'ESTATE_AGENT':
        return this.authRegister(email, password, 'ESTATE_AGENT').pipe(
          switchMap(() => {
            const payload: StafferRequest = { email };
            return this.estateAgentService.registerEstateAgent({
              body: payload,
            });
          }),
          map((agent: StafferResponse) => ({
            id: Number((agent as any)?.id) || Date.now(),
            email,
            role: 'ESTATE_AGENT' as const,
            active: true,
          })),
          catchError((error) => {
            console.error('[AdminDashboard] createUser(AGENT) error', error);
            return throwError(() => error);
          })
        );

      case 'ADMIN':
        return this.authRegister(email, password, 'ADMIN').pipe(
          switchMap(() => {
            const payload: StafferRequest = { email };
            return this.adminService.registerAdmin({ body: payload });
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

      default:
        return throwError(
          () => new Error(`Ruolo non supportato: ${role as string}`)
        );
    }
  }

  // ===== PASSWORD =====

  changePassword(
    currentPassword: string,
    newPassword: string
  ): Observable<void> {
    this.loading.set(true);
    this.success.set(null);
    this.error.set(null);

    return this.authService
      .changeAdminPassword({ oldPassword: currentPassword, newPassword })
      .pipe(
        tap(() => {
          this.success.set('Password aggiornata.');
          this.error.set(null);
        }),
        map(() => void 0),
        catchError((error) => {
          if (error?.status === 400) {
            this.error.set('La password corrente non è corretta.');
          } else if (error?.status === 401) {
            this.error.set('Sessione scaduta: accedi di nuovo.');
          } else if (error?.status === 403) {
            this.error.set('Non hai i permessi per questa operazione.');
          } else {
            this.error.set('Errore durante l’aggiornamento della password.');
          }
          return EMPTY;
        }),
        finalize(() => this.loading.set(false))
      );
  }
}
