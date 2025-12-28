import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CheckAccountExistsRequest } from '../../interfaces/check-account-exists-request.interface';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  httpClient = inject(HttpClient);
  
  baseUrl = `${environment.frontendBaseUrl}/api`;

  httpOptions: { headers: HttpHeaders } = { 
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  }

  checkAccountExists(request: CheckAccountExistsRequest){
    const url = `${this.baseUrl}/account/${request.email}`;
    return this.httpClient.get(url, this.httpOptions);
  }
}
