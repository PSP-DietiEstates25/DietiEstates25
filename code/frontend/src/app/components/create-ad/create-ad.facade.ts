import { Injectable, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';

import {
  GeographicalPositionControllerService as GeoSvc,
  CadastralDataControllerService as CadSvc,
  DetailControllerService as DetSvc,
  RealEstateControllerService as ReSvc,
} from '../../services/services';

export interface CreateAdDraft {
  title?: string;
  price?: number;

  address?: string;
  city?: string;
  municipality?: string;
  latitude?: number;
  longitude?: number;
  radius?: number | null;

  size?: number;
  rooms?: number;
  floor?: number;
  energyClass?: string;

  category?: string; 
  description?: string;

  imagesBase64: string[];
}

@Injectable({ providedIn: 'root' })
export class CreateAdFacade {
  draft = signal<CreateAdDraft>({ imagesBase64: [] });

  constructor(
    private geoApi: GeoSvc,
    private cadApi: CadSvc,
    private detApi: DetSvc,
    private reApi: ReSvc
  ) {}

  patchBasics(b: Partial<CreateAdDraft>) {
    this.draft.update((d) => ({ ...d, ...b }));
  }
  patchDetails(b: Partial<CreateAdDraft>) {
    this.draft.update((d) => ({ ...d, ...b }));
  }
  setImagesBase64(arr: string[]) {
    this.draft.update((d) => ({ ...d, imagesBase64: arr }));
  }
  reset() {
    this.draft.set({ imagesBase64: [] });
  }

  private async invoke<T>(
    svc: any,
    methodNames: string[],
    arg: any
  ): Promise<T> {
    for (const name of methodNames) {
      const fn = svc?.[name];
      if (typeof fn === 'function') {
        try {
          return await firstValueFrom(fn.call(svc, { body: arg }));
        } catch {
          /* try raw */
        }
        return await firstValueFrom(fn.call(svc, arg));
      }
    }
    throw new Error(
      `Metodo non trovato. Candidati provati: ${methodNames.join(', ')}`
    );
  }

  private idFrom(obj: any, keys: string[]) {
    for (const k of keys) if (obj && obj[k] != null) return obj[k];
    throw new Error(`ID non trovato. Chiavi provate: ${keys.join(', ')}`);
  }

  private getAgentEmailFromToken(): string | undefined {
    try {
      const raw = localStorage.getItem('auth.token');
      if (!raw) return;
      const payload = JSON.parse(atob(raw.split('.')[1] || ''));
      return payload?.sub ?? payload?.email ?? undefined;
    } catch {
      return;
    }
  }

  async submit(): Promise<void> {
    const d = this.draft();

    const geoPayload: any = {
      address: d.address ?? '',
      city: d.city ?? '',
      municipality: d.municipality ?? '',
      latitude: d.latitude ?? 0,
      longitude: d.longitude ?? 0,
      radius: d.radius ?? null,
      lat: d.latitude ?? 0,
      lon: d.longitude ?? 0,
    };

    const geoRes = await this.invoke<any>(
      this.geoApi,
      [
        'createGeographicalPosition',
        'createGeographical',
        'create',
        'save',
        'postGeographicalPosition',
      ],
      geoPayload
    );

    const geoId = this.idFrom(geoRes, [
      'id',
      'geoId',
      'geographicalPositionId',
    ]);

    const cadPayload: any = {
      price: d.price ?? 0,
      size: d.size ?? 0,
      rooms: d.rooms ?? 0,
      floor: d.floor ?? 0,
      energyClass: d.energyClass ?? 'ND',
      mq: d.size ?? 0,
      classEnergy: d.energyClass ?? 'ND',
    };

    const cadRes = await this.invoke<any>(
      this.cadApi,
      ['createCadastralData', 'create', 'save', 'postCadastralData'],
      cadPayload
    );

    const cadId = this.idFrom(cadRes, ['id', 'cadastralDataId', 'cadId']);

    const detPayload: any = {
      geographicalPositionId: geoId,
      cadastralDataId: cadId,
    };

    const detRes = await this.invoke<any>(
      this.detApi,
      ['createDetails', 'create', 'save', 'postDetails'],
      detPayload
    );

    const detailsId = this.idFrom(detRes, ['id', 'detailsId']);

    const email = this.getAgentEmailFromToken() ?? '';
    const rePayload: any = {
      category: d.category ?? 'Appartamento',
      type: d.category ?? 'Appartamento', 
      description: d.description || d.title || '',
      detailsId,
      estateAgentEmail: email,
      agentEmail: email, 
      images: d.imagesBase64 ?? [],
      photos: d.imagesBase64 ?? [],
    };

    await this.invoke<any>(
      this.reApi,
      ['createRealEstate', 'create', 'save', 'postRealEstate'],
      rePayload
    );
  }
}
