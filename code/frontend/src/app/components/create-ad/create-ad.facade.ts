import { Injectable, signal } from '@angular/core';
import { Observable, of, throwError, from } from 'rxjs';
import {
  map,
  switchMap,
  catchError,
  mergeMap,
  toArray,
} from 'rxjs/operators';

import {
  GeographicalPositionControllerService as GeoSvc,
  CadastralDataControllerService as CadSvc,
  DetailControllerService as DetSvc,
  RealEstateControllerService as ReSvc,
} from '../../services/services';

import { GeographicalPositionRequest } from '../../services/models/geographical-position-request';
import { CadastralDataRequest } from '../../services/models/cadastral-data-request';
import { DetailRequest } from '../../services/models/detail-request';
import { RealEstateRequest } from '../../services/models/real-estate-request';

export interface CreateAdDraft {
  // UI / campi usati dai componenti
  title?: string;
  price?: number;
  city?: string;
  address?: string;

  type?: string; 
  category?: string; 

  size?: number;
  rooms?: number;
  floor?: number;
  energy?: string; 
  energyClass?: string; 

  description?: string;

  latitude?: number;
  longitude?: number;

  photos?: File[];

  // immagini pronte per invio API
  imagesBase64?: string[];
}

@Injectable({ providedIn: 'root' })
export class CreateAdFacade {
  private _draft = signal<CreateAdDraft>({});

  constructor(
    private geoApi: GeoSvc,
    private cadApi: CadSvc,
    private detApi: DetSvc,
    private reApi: ReSvc
  ) {}

  // draft
  draft = this._draft.asReadonly();

  // patch serve a tutti gli step per aggiornare il draft parzialmente
  patchBasics(partial: Partial<CreateAdDraft>) {
    this._draft.update((d) => ({ ...d, ...partial }));
  }
  patchDetails(partial: Partial<CreateAdDraft>) {
    this._draft.update((d) => ({ ...d, ...partial }));
  }
  setImagesBase64(list: string[]) {
    this._draft.update((d) => ({ ...d, imagesBase64: list }));
  }
  setPhotos(files: File[]) {
    this._draft.update((d) => ({ ...d, photos: files }));
  }
  reset() {
    this._draft.set({});
  }

  // submit
  submit(): Observable<void> {
    const d = this._draft();
    const email = this.getCurrentEmail() || '';

    // 0) prepara immagini: se già presenti Base64 usale; altrimenti converte photos(File[]) -> Base64[]
    const images$: Observable<string[]> =
      d.imagesBase64 && d.imagesBase64.length
        ? of(d.imagesBase64)
        : d.photos && d.photos.length
        ? from(d.photos).pipe(
            mergeMap((f) => this.readFileAsDataURL$(f)),
            toArray()
          )
        : of([]);

    return images$.pipe(
      // 1) RealEstate
      switchMap((images) => {
        const rePayload: RealEstateRequest = {
          category: d.category || d.type || 'Appartamento',
          description: d.description || (d.title ?? ''),
          estateAgentEmail: email,
          images,
        };
        return this.reApi.createRealEstate$Response({ body: rePayload });
      }),
      map((res) => {
        const id = this.extractIdFromLocation(res.headers.get('Location'));
        if (id == null) throw new Error('Impossibile ottenere ID real estate');
        return id;
      }),

      // 2) Detail
      switchMap((realEstateId) => {
        const detailReq: DetailRequest = { realEstateId };
        return this.detApi.createDetail$Response({ body: detailReq }).pipe(
          map((res) => {
            const detailId = this.extractIdFromLocation(
              res.headers.get('Location')
            );
            if (detailId == null)
              throw new Error('Impossibile ottenere ID detail');
            return { realEstateId, detailId };
          })
        );
      }),

      // 3) GeographicalPosition
      switchMap(({ realEstateId, detailId }) => {
        const geoReq: GeographicalPositionRequest = {
          address: d.address || '',
          city: d.city || '',
          municipality: d.city || '',
          latitude: d.latitude as number,
          longitude: d.longitude as number,
        };
        return this.geoApi
          .createGeographicalPosition({ detailid: detailId, body: geoReq })
          .pipe(map(() => ({ realEstateId })));
      }),

      // 4) CadastralData
      switchMap(({ realEstateId }) => {
        const cadReq: CadastralDataRequest = {
          energyClass: d.energyClass || d.energy || 'ND',
          floor: d.floor ?? 0,
          price: d.price ?? 0,
          rooms: d.rooms ?? 0,
          squareMeters: d.size ?? 0,
        };
        return this.cadApi.createCadastralData({
          realestateid: realEstateId,
          body: cadReq,
        });
      }),

      map(() => void 0),
      catchError((err) => throwError(() => err))
    );
  }

  // helpers
  private extractIdFromLocation(location: string | null): number | null {
    if (!location) return null;
    const parts = location.split('/').filter(Boolean);
    const id = Number(parts[parts.length - 1]);
    return Number.isFinite(id) ? id : null;
  }

  private getCurrentEmail(): string | null {
    const token = localStorage.getItem('auth.token');
    const payload = this.decodeJwt(token);
    return (payload?.email as string) || (payload?.sub as string) || null;
  }

  private decodeJwt(token: string | null): any | null {
    try {
      if (!token) return null;
      const base = token.split('.')[1];
      const json = atob(base.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(json);
    } catch {
      return null;
    }
  }

  private readFileAsDataURL$(file: File): Observable<string> {
    return new Observable<string>((observer) => {
      const reader = new FileReader();
      reader.onload = () => {
        observer.next(String(reader.result));
        observer.complete();
      };
      reader.onerror = (e) => observer.error(e);
      reader.readAsDataURL(file);
    });
  }
}
