import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { EstateAgentAuthenticationControllerService } from '../../services/services/estate-agent-authentication-controller.service';
import { StafferRequest } from '../../services/models/staffer-request';
import { EstateAgent } from '../../services/models/estate-agent';

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

export interface ListAdsOpts {
  q?: string;
  active?: boolean | '';
}

@Injectable({ providedIn: 'root' })
export class AdminDashboardFacade {
  private http = inject(HttpClient);

  private agentAuth = inject(EstateAgentAuthenticationControllerService);

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
    name: string;
    email: string;
    role: Role;
    password?: string;
  }): Observable<AdminUser> {
    if (body.role === 'AGENT') {
      const adminEmail = localStorage.getItem('userEmail') ?? '';
      const password = body.password ?? 'ChangeMe123!';

      const reqLoose: any = {
        adminEmail,
        email: body.email,
        password,
      };
      reqLoose.role = 'AGENT';

      return this.agentAuth
        .registerEstateAgent({ body: reqLoose as StafferRequest })
        .pipe(
          map((ea: EstateAgent) => {
            const a: any = ea as any;
            const email = a.email ?? a.username ?? body.email;
            const name =
              body.name ||
              a.name ||
              a.username ||
              (a.email ? String(a.email).split('@')[0] : 'Agent');

            const active = typeof a.enabled === 'boolean' ? a.enabled : true;

            const createdAt: string | undefined =
              a.createdDate ?? a.lastModifiedDate ?? undefined;

            const id = (typeof a.id === 'number' ? a.id : 0) as number;

            const u: AdminUser = {
              id,
              name,
              email,
              role: 'AGENT',
              active,
              createdAt,
            };
            return u;
          })
        );
    }
    return this.http.post<AdminUser>('/api/admin/users', body);
  }

  updateUser(
    id: number,
    patch: Partial<Pick<AdminUser, 'active' | 'role'>>
  ): Observable<void> {
    return this.http.patch<void>(`/api/admin/users/${id}`, patch);
  }
}
