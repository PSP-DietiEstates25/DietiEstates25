import { Inject, Injectable, InjectionToken } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export const AUTH_API_ROOT = new InjectionToken<string>('AUTH_API_ROOT');

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

@Injectable({ providedIn: 'root' })
export class AuthAccountService {
  constructor(
    private http: HttpClient,
    @Inject(AUTH_API_ROOT) private rootUrl: string
  ) {}

  private getAccessToken(): string | null {
    return (
      localStorage.getItem('access_token') ||
      localStorage.getItem('token') ||
      localStorage.getItem('auth.token') ||
      null
    );
  }

  changePassword(body: ChangePasswordRequest): Observable<void> {
    const url = `${this.rootUrl}/account/password`;

    const token = this.getAccessToken();
    const headers = token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : undefined;

    return this.http
      .patch<void>(url, body, {
        headers,
        // withCredentials: true,
        observe: 'response',
      })
      .pipe(map(() => void 0));
  }
}
