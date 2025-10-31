import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AutentServiceService {

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
    email: string,
    password: string,
    role?: string,
  }) {
    const url = 'http://localhost:8080/auth/register';
    return this.httpClient.post<string>(url, registerRequest, this.httpOptions);
  }
}
