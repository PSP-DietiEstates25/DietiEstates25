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
import { EMPTY, of, from, Observable } from 'rxjs';

import {
  GeographicalPositionControllerService as GeoSvc,
  CadastralDataControllerService as CadSvc,
  DetailControllerService as DetSvc,
  UtilityControllerService as UtlSvc,
  RealEstateControllerService as ReSvc,
} from '../../services/resource_server/services';

import { GeographicalPositionRequest } from '../../services/resource_server/models';
import { CadastralDataRequest } from '../../services/resource_server/models';
import { DetailRequest } from '../../services/resource_server/models';
import { UtilityRequest } from '../../services/resource_server/models';
import { RealEstateRequest } from '../../services/resource_server/models';

export type Category = 'SALE' | 'RENT';

export interface BasicsDraft {
  category: Category;
  description: string;
}

export interface UtilitiesDraft {
  hasElevator: boolean;
  hasDoorman: boolean;
  hasAirConditioning: boolean;
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
  private geo = inject(GeoSvc);
  private cad = inject(CadSvc);
  private det = inject(DetSvc);
  private utl = inject(UtlSvc);
  private re = inject(ReSvc);
  private router = inject(Router);

  basics = signal<BasicsDraft | null>(null);
  utilities = signal<UtilitiesDraft | null>(null);
  position = signal<PositionDraft | null>(null);
  cadastral = signal<CadastralDraft | null>(null);
  images = signal<File[]>([]);

  loading = signal(false);
  error = signal<string | null>(null);

  allValid = computed(
    () =>
      !!(
        this.basics() &&
        this.utilities() &&
        this.position() &&
        this.cadastral() &&
        this.images().length
      )
  );

  setBasics(v: BasicsDraft) {
    this.basics.set(v);
  }
  setUtilities(v: UtilitiesDraft) {
    this.utilities.set(v);
  }
  setPosition(v: PositionDraft) {
    this.position.set(v);
  }
  setCadastral(v: CadastralDraft) {
    this.cadastral.set(v);
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

  resetDraft() {
    this.basics.set(null);
    this.utilities.set(null);
    this.position.set(null);
    this.cadastral.set(null);
    this.images.set([]);
    this.error.set(null);
    this.loading.set(false);
  }

  cancel() {
    this.resetDraft();
    this.router.navigateByUrl('/agent');
  }

  createAd() {
    const basics = this.basics();
    const util = this.utilities();
    const pos = this.position();
    const cad = this.cadastral();
    const imgs = this.images();
    const agentEmail = this.getAgentEmail();

    if (!basics || !util || !pos || !cad) {
      this.error.set('Compila tutti gli step prima di pubblicare.');
      return;
    }

    if (!agentEmail) {
      this.error.set(
        "Impossibile ottenere l'email dell'agente. Effettua nuovamente il login."
      );
      return;
    }

    const utilityReq: UtilityRequest = {
      hasAirConditioning: !!util.hasAirConditioning,
      hasDoorman: !!util.hasDoorman,
      hasElevator: !!util.hasElevator,
    };

    const gpReq: GeographicalPositionRequest = {
      address: pos.address,
      city: pos.city,
      municipality: pos.municipality,
      latitude: pos.latitude,
      longitude: pos.longitude,
      radius: pos.radius ?? 0,
    };

    const cadReq: CadastralDataRequest = {
      price: cad.price,
      rooms: cad.rooms,
      floor: cad.floor,
      energyClass: cad.energyClass,
      squareMeters: cad.squareMeters,
    } as CadastralDataRequest;

    this.loading.set(true);
    this.error.set(null);

    this.utl
      .createUtility$Response({ body: utilityReq })
      .pipe(
        // utilityId: number
        map((resp): number => {
          const id = this.extractId(resp, ['utilityId']);
          if (id == null) throw new Error('Utility creata ma ID non presente');
          return id;
        }),

        // { utilityId, gpId }: entrambi number
        switchMap((utilityId: number) =>
          this.geo.createGeographicalPosition$Response({ body: gpReq }).pipe(
            map((resp): { utilityId: number; gpId: number } => {
              const gpId = this.extractId(resp, ['geographicalPositionId']);
              if (gpId == null)
                throw new Error(
                  'GeographicalPosition creata ma ID non presente'
                );
              return { utilityId, gpId };
            })
          )
        ),

        // { detailId }
        switchMap(({ utilityId, gpId }) =>
          this.det
            .createDetail$Response({
              body: { geographicalPositionId: gpId, utilityId },
            })
            .pipe(
              map((resp): { detailId: number } => {
                const detailId = this.extractId(resp, ['detailId']);
                if (detailId == null)
                  throw new Error('Detail creato ma ID non presente');
                return { detailId };
              })
            )
        ),

        // { detailId, cadastralId }
        switchMap(({ detailId }) =>
          this.cad.createCadastralData$Response({ body: cadReq }).pipe(
            map((resp): { detailId: number; cadastralId: number } => {
              const cadastralId = this.extractId(resp, ['cadastralDataId']);
              if (cadastralId == null)
                throw new Error('Cadastral creato ma ID non presente');
              return { detailId, cadastralId };
            })
          )
        ),

        // realEstateId: number
        switchMap(({ detailId, cadastralId }) =>
          this.readFilesAsDataURL$(imgs).pipe(
            map((s) => this.dataUrlToBase64(s)),
            toArray(),
            switchMap((imagesBase64: string[]) =>
              this.re
                .createRealEstate$Response({
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
                  map((resp): number => {
                    const realEstateId = this.extractId(resp, ['realEstateId']);
                    if (realEstateId == null)
                      throw new Error('Annuncio creato ma ID non presente');
                    return realEstateId;
                  })
                )
            )
          )
        ),

        catchError((e) => {
          this.error.set(
            e?.error?.message || e?.message || 'Creazione annuncio fallita'
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false))
      )
      .subscribe((realEstateId: number) => {
        // Arrivi qui solo se tutto OK
        this.resetDraft();
        this.router.navigateByUrl('/agent');
      });
  }

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
    return from(files).pipe(mergeMap((f) => this.readFileAsDataURL$(f)));
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

  private dataUrlToBase64(dataUrl: string): string {
    const i = dataUrl.indexOf(',');
    const s = i >= 0 ? dataUrl.slice(i + 1) : dataUrl;
    return s.replace(/\s/g, '');
  }

  private extractId(resp: any, fallbackKeys: string[] = []): number | null {
    const body = resp?.body ?? resp;
    const keys = ['id', ...fallbackKeys];
    for (const k of keys) {
      const v = body?.[k];
      if (v != null && !Number.isNaN(Number(v))) return Number(v);
    }
    const headers = resp?.headers;
    const loc = headers?.get?.('Location') || headers?.get?.('location');
    if (loc) {
      const m = String(loc).match(/\/(\d+)(?!.*\d)/);
      if (m) return Number(m[1]);
    }
    return null;
  }
}
