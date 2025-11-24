import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { map } from 'rxjs';

export type NearTag = 'NEAR_PARKS' | 'NEAR_PUBLIC_TRANSPORT' | 'NEAR_SCHOOLS';

export interface GeoapifyFeatureProperties {
  categories: string[];
}

export interface GeoapifyFeature {
  properties: GeoapifyFeatureProperties;
}

export interface GeoapifyPlacesResponse {
  features: GeoapifyFeature[];
}

@Injectable({
  providedIn: 'root',
})
export class GeoapifyService {
  
  private httpClient = inject(HttpClient);

  getLatitudeLongitudeData(latitude: number, longitude: number){
    //const url2 = `https://api.geoapify.com/v1/geocode/reverse?lat=${latitude}&lon=${longitude}&format=json&apiKey=${environment.geoapifyAPIKey}`
    const url = "https://api.geoapify.com/v1/geocode/reverse";
    const params = new HttpParams()
      .set('lat', latitude)
      .set('lon', longitude)
      .set('format', 'json')
      .set('apiKey', longitude)

    return this.httpClient.get(url);
  }

  getNearPlacesByLatitudeLongitude(latitude: number,longitude: number) {

    const url = `https://api.geoapify.com/v2/places` +
      `?categories=education.school,public_transport,leisure.park` +
      `&filter=circle:${longitude},${latitude},${environment.placesRadius}` + // attenzione ordine lon,lat
      `&bias=proximity:${longitude},${latitude}` +
      `&limit=${environment.placesLimit}` +
      `&apiKey=${environment.geoapifyAPIKey}`;

    return this.httpClient.get<GeoapifyPlacesResponse>(url)
      .pipe(
        map((response) => {
          const features = response.features ?? [];

          const hasParks = features.some((feature) =>
            feature.properties.categories.includes('leisure.park')
          );

          const hasPublicTransport = features.some((feature) =>
            feature.properties.categories.some((category) =>
              category === 'public_transport' || category.startsWith('public_transport.')
            )
          );

          const hasSchools = features.some((feature) =>
            feature.properties.categories.includes('education.school')
          );

          const tags: NearTag[] = [];

          if (hasParks) tags.push('NEAR_PARKS');
          if (hasPublicTransport) tags.push('NEAR_PUBLIC_TRANSPORT');
          if (hasSchools) tags.push('NEAR_SCHOOLS');

          return tags;
        })
      );
  }

  getConsistOfPlaceBoundaries(placeId: number){
    const url = `https://api.geoapify.com/v1/boundaries/consists-of?id=${placeId}&geometry=geometry_1000&apiKey=${environment.geoapifyAPIKey}`;
    return this.httpClient.get(url);
  }

  getCountryPlaceId(placeName: string){
    const encodedPlaceName = encodeURIComponent(placeName);
    const url = `https://api.geoapify.com/v1/geocode/search?country=${encodedPlaceName}&apiKey=${environment.geoapifyAPIKey}`;
    return this.httpClient.get(url);
  }

  get
}
