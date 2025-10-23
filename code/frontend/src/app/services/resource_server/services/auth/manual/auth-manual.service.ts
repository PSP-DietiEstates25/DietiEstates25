import { HttpClient, HttpHeaderResponse, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthManualService {
  
  token: string = "";
  httpClient = inject(HttpClient);

  get(url: string){
    return this.httpClient.get("http://localhost:9090" + url);
  }

  getPrivate(url: string){
    return this.httpClient.get("http://localhost:9090" + url, {
      headers: new HttpHeaders({"Authorization": "Bearer" + this.token})
    });
  }

}
