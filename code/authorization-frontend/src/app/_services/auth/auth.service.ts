import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  
  httpClient = inject(HttpClient);

  httpOptions: { headers: HttpHeaders } = {
    headers: new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Accept': 'application/json',
      'X-Requested-With': 'XMLHttpRequest',
    })
  };

  login(body: HttpParams){
    return this.httpClient.post<{ redirectUrl: string }>(environment.loginProcessingUrl, body.toString(), this.httpOptions);
  }
}