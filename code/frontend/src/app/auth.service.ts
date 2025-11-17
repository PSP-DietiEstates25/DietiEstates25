import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AccountResponse } from './components/admin-dashboard/admin-dashboard.facade';
import { LocalStorageService } from './services/services/local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly localStorageService = inject(LocalStorageService);

  httpClient = inject(HttpClient);

  url = 'http://localhost:8080/userinfo';
  httpOptions: { headers: HttpHeaders, withCredentials: boolean } = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    }),
    withCredentials: true
  };

  getUserInfo(){
    return this.httpClient.get<any>(this.url, this.httpOptions);
  }

  logout(){
    return this.httpClient.post('http://localhost:8080/logout', null, { withCredentials: true });
  }

  getCsrf(){
    return  this.httpClient.get('http://localhost:8080/csrf-token', { withCredentials: true })
  }

  register(registerRequest: {
    email?: string,
    password?: string,
    role?: string,
  }) {
    const url = 'http://localhost:8080/auth/register';
    return this.httpClient.post<AccountResponse>(url, registerRequest, this.httpOptions);
  }

  changeAdminPassword(changeAdminPasswordRequest: {
    oldPassword?: string,
    newPassword?: string
  }){
    const url = 'http://localhost:8080/account/password';
    return this.httpClient.patch(url, changeAdminPasswordRequest, this.httpOptions);
  }

  isEstateAgent(){
    return this.localStorageService.getItem("role") === "ESTATE_AGENT";
  }

  isAdmin(){
    return this.localStorageService.getItem("role") === "ADMIN";
  }

  isUser(){
    return this.localStorageService.getItem("role") === "USER";
  }
}
