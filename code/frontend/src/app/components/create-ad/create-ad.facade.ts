import { Injectable, signal, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import {
  catchError,
  map,
  mergeMap,
  switchMap,
  toArray,
  finalize,
} from 'rxjs/operators';
import { EMPTY, of, from, Observable, Subject } from 'rxjs';

import {
  GeographicalPositionControllerService,
  CadastralDataControllerService,
  DetailControllerService,
  UtilityControllerService,
  RealEstateControllerService,
} from '../../services/services';

import { GeographicalPositionRequest } from '../../services/models/geographical-position-request';
import { CadastralDataRequest } from '../../services/models/cadastral-data-request';
import { DetailRequest } from '../../services/models/detail-request';
import { UtilityRequest } from '../../services/models/utility-request';
import { RealEstateRequest } from '../../services/models/real-estate-request';

export type Category = 'SALE' | 'RENT';

export interface BasicsDraft {
  category: Category;
  description: string;
}

export interface UtilitiesDraft {
  hasElevator: boolean;
  hasDoorman: boolean;
  hasAirConditioning: boolean;

  nearPark: boolean;
  nearPublicTransport: boolean;
  nearSchool: boolean;
}

export interface PositionDraft {
  address: string;
  city: string;
  municipality: string;
  latitude: number;
  longitude: number;
  radius?: number;
}

export interface CadastralDraft {
  price: number;
  rooms: number;
  floor: number;
  energyClass: 'A4' | 'A3' | 'A2' | 'A1' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G';
  squareMeters: number;
}

@Injectable({ providedIn: 'root' })
export class CreateAdFacade {
  private geographicalPositionService = inject(GeographicalPositionControllerService);
  private cadastralDataService = inject(CadastralDataControllerService);
  private detailService = inject(DetailControllerService);
  private utlilityService = inject(UtilityControllerService);
  private realEstateService = inject(RealEstateControllerService);
  private routerService = inject(Router);

  private publishedSubject = new Subject<number>();

  published$ = this.publishedSubject.asObservable();
  basics = signal<BasicsDraft | null>(null);
  utility = signal<UtilitiesDraft | null>(null);
  geographicalPosition = signal<PositionDraft | null>(null);
  cadastralData = signal<CadastralDraft | null>(null);
  images = signal<File[]>([]);

  loading = signal(false);
  error = signal<string | null>(null);

  allValid = computed(
    () =>
      !!(
        this.basics() &&
        this.utility() &&
        this.geographicalPosition() &&
        this.cadastralData() &&
        this.images().length
      )
  );

  getBasics(){
    return this.basics();
  }

  getUtility(){
    return this.utility();
  }

  getGeographicalPosition(){
    return this.geographicalPosition();
  }

  getCadastralData(){
    return this.cadastralData();
  }

  getImages(){
    return this.images();
  }

  setBasics(basicDraft: BasicsDraft) {
    this.basics.set(basicDraft);
  }

  setUtilities(utilityDraft: UtilitiesDraft) {
    this.utility.set(utilityDraft);
  }

  setPosition(geographicalPositionDraft: PositionDraft) {
    this.geographicalPosition.set(geographicalPositionDraft);
  }

  setCadastral(cadastralDataDraft: CadastralDraft) {
    this.cadastralData.set(cadastralDataDraft);
  }

  setImages(files: File[]) {
    this.images.set(files ?? []);
  }

  addImages(files: File[]) {
    this.images.set([...(this.images() ?? []), ...(files ?? [])]);
  }

  removeImage(index: number) {
    const arr = [...(this.images() ?? [])];
    arr.splice(index, 1);
    this.images.set(arr);
  }

  createAd() {

    const basics = this.basics();
    const utility = this.utility();
    const geographicalPosition = this.geographicalPosition();
    const cadastralData = this.cadastralData();
    const imgs = this.images();
    const agentEmail = this.getAgentEmail();

    if (!basics || !utility || !geographicalPosition || !cadastralData) {
      this.error.set('Compila tutti gli step prima di pubblicare.');
      return;
    }

    const utilityRequest: UtilityRequest = {
      hasAirConditioning: !!utility.hasAirConditioning,
      hasDoorman: !!utility.hasDoorman,
      hasElevator: !!utility.hasElevator,
      nearPark: !!utility.nearPark,
      nearPublicTransport: !!utility.nearPublicTransport,
      nearSchool: !!utility.nearSchool,
    };

    const geographicalPositionRequest: GeographicalPositionRequest = {
      address: geographicalPosition.address,
      city: geographicalPosition.city,
      municipality: geographicalPosition.municipality,
      latitude: geographicalPosition.latitude,
      longitude: geographicalPosition.longitude,
      radius: geographicalPosition.radius ?? 0,
    };

    const cadastralDataRequest: CadastralDataRequest = {
      price: cadastralData.price,
      rooms: cadastralData.rooms,
      floor: cadastralData.floor,
      energyClass: cadastralData.energyClass,
      squareMeters: cadastralData.squareMeters,
    } as CadastralDataRequest;

    this.loading.set(true);
    this.error.set(null);

    this.utlilityService.createUtility$Response({ body: utilityRequest })
      .pipe(
        // utilityId: number
        map((response): number => {
          const id = this.extractId(response, ['utilityId']);
          if (id == null) throw new Error('Utility creata ma ID non presente');
          return id;
        }),

        // { utilityId, gpId }: entrambi number
        switchMap((utilityId: number) =>
          this.geographicalPositionService.createGeographicalPosition$Response({ body: geographicalPositionRequest }).pipe(
            map((response): { utilityId: number; geographicalPositionId: number } => {
              const geographicalPositionId = this.extractId(response, ['geographicalPositionId']);
              if (geographicalPositionId == null)
                throw new Error(
                  'GeographicalPosition creata ma ID non presente'
                );
              return { utilityId, geographicalPositionId };
            })
          )
        ),

        // { detailId }
        switchMap(({ utilityId, geographicalPositionId }) =>
          this.detailService.createDetail$Response({
              body: { geographicalPositionId: geographicalPositionId, utilityId },
            })
            .pipe(
              map((response): { detailId: number } => {
                const detailId = this.extractId(response, ['detailId']);
                if (detailId == null)
                  throw new Error('Detail creato ma ID non presente');
                return { detailId };
              })
            )
        ),

        // { detailId, cadastralId }
        switchMap(({ detailId }) =>
          this.cadastralDataService.createCadastralData$Response({ body: cadastralDataRequest }).pipe(
            map((response): { detailId: number; cadastralId: number } => {
              const cadastralId = this.extractId(response, ['cadastralDataId']);
              if (cadastralId == null)
                throw new Error('Cadastral creato ma ID non presente');
              return { detailId, cadastralId };
            })
          )
        ),

        // realEstateId: number
        switchMap(({ detailId, cadastralId }) =>
          this.readFilesAsDataURL$(imgs).pipe(
            map((url) => this.dataUrlToBase64(url)),
            toArray(),
            switchMap((imagesBase64: string[]) =>
              this.realEstateService.createRealEstate$Response({
                  body: {
                    detailId,
                    cadastralDataId: cadastralId,
                    category: basics.category,
                    description: basics.description,
                    estateAgentEmail: agentEmail,
                    images: imagesBase64,
                  } as RealEstateRequest,
                })
                .pipe(
                  map((response): number => {
                    const realEstateId = this.extractId(response, ['realEstateId']);
                    if (realEstateId == null)
                      throw new Error('Annuncio creato ma ID non presente');
                    return realEstateId;
                  })
                )
            )
          )
        ),

        catchError((error) => {
          this.error.set(
            error?.error?.message || error?.message || 'Creazione annuncio fallita'
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false))
      )
      .subscribe((realEstateId: number) => {
        this.publishedSubject.next(realEstateId);
        this.routerService.navigateByUrl('/');
      });
  }

  clearSavedData(){
    this.basics.set(null);
    this.utility.set(null);
    this.geographicalPosition.set(null);
    this.cadastralData.set(null);
    this.images.set([]);
  }

  //da eliminare, non recuperiamo nulla dal local storage, al massimo il ruolo
  private getAgentEmail(): string | null {
    try {
      const stored = localStorage.getItem('userEmail');
      if (stored) return stored;
      const token = localStorage.getItem('auth.token');
      if (!token) return null;
      const base = token.split('.')[1];
      const json = atob(base.replace(/-/g, '+').replace(/_/g, '/'));
      const payload = JSON.parse(json);
      return payload.email || payload.sub || null;
    } catch {
      return null;
    }
  }

  // ---- utils ----
  private readFilesAsDataURL$(files: File[]): Observable<string> {
    return from(files).pipe(mergeMap((file) => this.readFileAsDataURL$(file)));
  }

  private readFileAsDataURL$(file: File): Observable<string> {
    return new Observable<string>((observer) => {
      const reader = new FileReader();
      reader.onload = () => {
        observer.next(String(reader.result));
        observer.complete();
      };
      reader.onerror = (error) => observer.error(error);
      reader.readAsDataURL(file);
    });
  }

  private dataUrlToBase64(dataUrl: string): string {
    const i = dataUrl.indexOf(',');
    const s = i >= 0 ? dataUrl.slice(i + 1) : dataUrl;
    return s.replace(/\s/g, '');
  }

  private extractId(response: any, fallbackKeys: string[] = []): number | null {
    const body = response?.body ?? response;
    const keys = ['id', ...fallbackKeys];
    for (const k of keys) {
      const v = body?.[k];
      if (v != null && !Number.isNaN(Number(v))) return Number(v);
    }
    const headers = response?.headers;
    const location = headers?.get?.('Location') || headers?.get?.('location');
    if (location) {
      const m = String(location).match(/\/(\d+)(?!.*\d)/);
      if (m) return Number(m[1]);
    }
    return null;
  }
}
