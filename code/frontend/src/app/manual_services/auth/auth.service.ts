import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LocalStorageService } from '../local-storage/local-storage.service';
import { AccountResponse } from '../../interfaces/account-response';
import { environment } from '../../../environments/environment';

export interface UserInfo {
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiBaseUrl;
  private csrfUrl = `${environment.apiBaseUrl}/api/csrf-token`;
  private registerUrl = `${environment.apiBaseUrl}/api/auth/register`;
  private logoutUrl = `${environment.apiBaseUrl}/api/logout`;
  private userInfoUrl = `${environment.apiBaseUrl}/api/userinfo`;
  private changeAdminPasswordUrl = `${environment.apiBaseUrl}/api/account/password`;

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
