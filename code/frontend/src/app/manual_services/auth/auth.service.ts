import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LocalStorageService } from '../local-storage/local-storage.service';
import { AccountResponse } from '../../interfaces/account-response';

export interface UserInfo {
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly localStorageService = inject(LocalStorageService);
  private userInfo = signal<UserInfo | null>(null);

  httpClient = inject(HttpClient);

  url = 'http://localhost:8080/userinfo';
  httpOptions: { headers: HttpHeaders; withCredentials: boolean } = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
    withCredentials: true,
  };

  getUserInfo() {
    return this.httpClient.get<any>(this.url, this.httpOptions);
  }

  logout() {
    return this.httpClient.post('http://localhost:8080/logout', null, {
      withCredentials: true,
    });
  }

  getCsrf() {
    return this.httpClient.get('http://localhost:8080/csrf-token', {
      withCredentials: true,
    });
  }

  register(registerRequest: {
    email?: string;
    password?: string;
    role?: string;
  }) {
    const url = 'http://localhost:8080/auth/register';
    return this.httpClient.post<AccountResponse>(
      url,
      registerRequest,
      this.httpOptions,
    );
  }

  changeAdminPassword(changeAdminPasswordRequest: {
    oldPassword?: string;
    newPassword?: string;
  }) {
    const url = 'http://localhost:8080/account/password';
    return this.httpClient.patch(
      url,
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
