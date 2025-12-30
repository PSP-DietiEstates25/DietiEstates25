import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { LocalStorageService } from '../local-storage/local-storage.service';
import { AccountResponse } from '../../interfaces/account-response';
import { environment } from '../../../environments/environment';
import { ApiConfiguration } from '../../services/api-configuration';
import { catchError, of, throwError, map } from 'rxjs';

export interface UserInfo {
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiConfiguration = inject(ApiConfiguration);

  private apiUrl = environment.apiBaseUrl;
  private csrfUrl = `${this.apiConfiguration.rootUrl}/csrf-token`;
  private registerUrl = `${this.apiConfiguration.rootUrl}/auth/register`;
  private logoutUrl = `${this.apiConfiguration.rootUrl}/logout`;
  private userInfoUrl = `${this.apiConfiguration.rootUrl}/userinfo`;
  private changeAdminPasswordUrl = `${this.apiConfiguration.rootUrl}/account/password`;
  private checkAccountExistsUrl = `${this.apiConfiguration.rootUrl}/account`;

  private readonly localStorageService = inject(LocalStorageService);
  private userInfo = signal<UserInfo | null>(null);

  httpClient = inject(HttpClient);

  httpOptions: { headers: HttpHeaders; withCredentials: boolean } = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
    withCredentials: true,
  };

  getUserInfo() {
    return this.httpClient.get<any>(this.userInfoUrl, this.httpOptions);
  }

  logout() {
    return this.httpClient.post(this.logoutUrl, null, {
      withCredentials: true,
    });
  }

  getCsrf() {
    return this.httpClient.get(this.csrfUrl, {
      withCredentials: true,
    });
  }

  register(registerRequest: {
    email?: string;
    password?: string;
    role?: string;
  }) {
    return this.httpClient.post<AccountResponse>(
      this.registerUrl,
      registerRequest,
      this.httpOptions,
    ).pipe(
      map(() => true),
      catchError((response: HttpErrorResponse) => {
        const body = response.error as any;
        if(response.status === 403 && body.businessErrorCode === 1403){
          return of(false)
        }

        return throwError(() => response);
      })
    );
  }

  changeAdminPassword(changeAdminPasswordRequest: {
    oldPassword?: string;
    newPassword?: string;
  }) {
    return this.httpClient.patch(
      this.changeAdminPasswordUrl,
      changeAdminPasswordRequest,
      this.httpOptions,
    );
  }

  isAuthenticated() {
    return this.localStorageService.getItem('isAuthenticated') == 'true'
      ? true
      : false;
  }

  isEstateAgent() {
    return this.localStorageService.getItem('role') === 'ESTATE_AGENT';
  }

  isAdmin() {
    return this.localStorageService.getItem('role') === 'ADMIN';
  }

  isUser() {
    return (
      this.localStorageService.getItem('role') === 'USER' ||
      this.localStorageService.getItem('role') === 'OIDC_USER'
    );
  }

  setRole(role: string) {
    this.localStorageService.setItem('role', role);
  }

  getRole() {
    return this.localStorageService.getItem('role');
  }

  setUserInfo(info: UserInfo | null) {
    this.userInfo.set(info);
  }

  getInfo() {
    return this.userInfo();
  }

  getEmail() {
    return this.userInfo()?.email;
  }
}
