import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs';
import * as L from 'leaflet';

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

  getLatitudeLongitudeData(latitude: number, longitude: number) {
    const url = 'https://api.geoapify.com/v1/geocode/reverse';
    const params = new HttpParams()
      .set('lat', latitude.toString())
      .set('lon', longitude.toString())
      .set('format', 'json')
      .set('lang', 'it')
      .set('apiKey', environment.geoapifyAPIKey);

    return this.httpClient.get(url, { params });
  }

  getNearPlacesByLatitudeLongitude(latitude: number, longitude: number) {
    const url = 'https://api.geoapify.com/v2/places';
    const params = new HttpParams()
      .set('categories', 'education.school,public_transport,leisure.park')
      .set(
        'filter',
        `circle:${longitude.toString()},${latitude.toString()},${environment.placesRadius.toString()}`,
      )
      .set('bias', `proximity:${longitude.toString()},${latitude.toString()}`)
      .set('limit', environment.placesLimit.toString())
      .set('lang', 'it')
      .set('apiKey', environment.geoapifyAPIKey);

    return this.httpClient.get<GeoapifyPlacesResponse>(url, { params }).pipe(
      map((response) => {
        const features = response.features ?? [];

        const hasParks = features.some((feature) =>
          feature.properties.categories.includes('leisure.park'),
        );

        const hasPublicTransport = features.some((feature) =>
          feature.properties.categories.some(
            (category) =>
              category === 'public_transport' ||
              category.startsWith('public_transport.'),
          ),
        );

        const hasSchools = features.some((feature) =>
          feature.properties.categories.includes('education.school'),
        );

        const tags: NearTag[] = [];

        if (hasParks) tags.push('NEAR_PARKS');
        if (hasPublicTransport) tags.push('NEAR_PUBLIC_TRANSPORT');
        if (hasSchools) tags.push('NEAR_SCHOOLS');

        return tags;
      }),
    );
  }

  getPlaceByCityAndRegion(cityName: string, regionName: string) {
    const url = `https://api.geoapify.com/v1/geocode/search`;
    const params = new HttpParams()
      .set('city', cityName)
      .set('state', regionName)
      .set('country', 'Italy')
      .set('limit', '1')
      .set('format', 'json')
      .set('lang', 'it')
      .set('apiKey', environment.geoapifyAPIKey);

    return this.httpClient.get<any>(url, { params });
  }

  getPlaceIdByCityAndRegion(cityName: string, regionName: string) {
    return this.getPlaceByCityAndRegion(cityName, regionName).pipe(
      map((response) => {
        console.log('place_id: ' + response.results?.[0]?.place_id);
        return response.results?.[0]?.place_id || null;
      }),
    );
  }

  getCounterIcon(count: number): L.Icon {
    const apiKey = environment.geoapifyAPIKey || '';

    const params = new URLSearchParams({
      apiKey: apiKey,
      type: 'material',
      color: '#094585',
      text: count.toString(),
      textColor: '#ffffff',
      textSize: 'small',
      scaleFactor: '2',
    });

    return L.icon({
      iconUrl: `https://api.geoapify.com/v1/icon?${params.toString()}`,
      iconSize: [31, 46],
      iconAnchor: [15.5, 46],
      popupAnchor: [0, -46],
      // shadowUrl: environmentMap.map_house_marker_shadow
    });
  }

  getMunicipalityName(lat: number, lon: number) {
    const url = `https://api.geoapify.com/v1/boundaries/part-of`;
    const params = new HttpParams()
      .set('lat', lat.toString())
      .set('lon', lon.toString())
      .set('subType', 'district')
      .set('geometry', 'geometry_1000')
      .set('apiKey', environment.geoapifyAPIKey)
      .set('lang', 'it');

    return this.httpClient.get<any>(url, { params }).pipe(
      map((response) => {
        const features = response.features || [];

        if (features.length > 0) {
          return features[0].properties.name;
        } else return features[0].properties.city;
      }),
    );
  }

  getCityMunicipality(placeId: string) {
    const url = `https://api.geoapify.com/v1/boundaries/consists-of`;
    const params = new HttpParams()
      .set('id', placeId)
      .set('geometry', 'geometry_1000')
      .set('lang', 'it')
      .set('apiKey', environment.geoapifyAPIKey);

    return this.httpClient.get<any>(url, { params });
  }

  getPlaceDetailsGeometry(placeId: string) {
    const url = `https://api.geoapify.com/v2/place-details`;
    const params = new HttpParams()
      .set('id', placeId)
      .set('features', 'details,details.geometry')
      .set('lang', 'it')
      .set('apiKey', environment.geoapifyAPIKey);

    return this.httpClient.get<any>(url, { params });
  }
}
