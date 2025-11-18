import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class GeoapifyService {
  
  private httpClient = inject(HttpClient);

  getLatitudeLongitudeData(latitude: number, longitude: number){
    const url = `https://api.geoapify.com/v1/geocode/reverse?lat=${latitude}&lon=${longitude}&format=json&apiKey=${environment.geoapifyAPIKey}`
    return this.httpClient.get(url);
  }
}
