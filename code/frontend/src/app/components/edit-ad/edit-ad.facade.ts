import { Injectable, signal, computed, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EMPTY, from, Observable, Subject } from 'rxjs';
import {
  catchError,
  finalize,
  map,
  mergeMap,
  switchMap,
  toArray,
} from 'rxjs/operators';

import {
  GeographicalPositionControllerService as GeoSvc,
  CadastralDataControllerService as CadSvc,
  DetailControllerService as DetSvc,
  UtilityControllerService as UtlSvc,
  RealEstateControllerService as ReSvc,
} from '../../services/services';

import {
  GeographicalPositionRequest,
  CadastralDataRequest,
  DetailRequest,
  RealEstateRequest,
  RealEstateResponse,
  UtilityRequest,
} from '../../services/models';

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

@Injectable()
export class EditAdFacade {
  private detailId = signal<number | null>(null);
  private geoId = signal<number | null>(null);
  private utilityId = signal<number | null>(null);
  private cadastralId = signal<number | null>(null);

  private geo = inject(GeoSvc);
  private cad = inject(CadSvc);
  private det = inject(DetSvc);
  private utl = inject(UtlSvc);
  private re = inject(ReSvc);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  readonly mode = 'edit' as const;

  basics = signal<BasicsDraft | null>(null);
  utilities = signal<UtilitiesDraft | null>(null);
  position = signal<PositionDraft | null>(null);
  cadastral = signal<CadastralDraft | null>(null);
  images = signal<File[]>([]);

  loading = signal(false);
  error = signal<string | null>(null);

  private savedSubject = new Subject<number>();
  saved$ = this.savedSubject.asObservable();

  private editingId = signal<number | null>(null);

  allValid = computed(
    () =>
      !!(
        this.basics() &&
        this.utilities() &&
        this.position() &&
        this.cadastral() &&
        this.images().length > 0
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
  removeImage(i: number) {
    this.images.update((arr) => arr.filter((_, idx) => idx !== i));
  }
  setImages(files: File[]) {
    this.images.set(files ?? []);
  }
  addImages(files: File[]) {
    this.images.set([...(this.images() ?? []), ...(files ?? [])]);
  }

  load(realEstateId: number) {
    if (Number.isNaN(realEstateId)) {
      const p = this.route.snapshot.paramMap.get('realestateId');
      realEstateId = p ? Number(p) : NaN;
    }
    if (Number.isNaN(realEstateId)) {
      this.error.set('ID annuncio non valido nell’URL.');
      return;
    }

    this.editingId.set(realEstateId);
    this.loading.set(true);
    this.error.set(null);

    this.re
      .getRealEstateById$Response({ realestateid: realEstateId })
      .pipe(
        map((r) => r.body as RealEstateResponse),
        switchMap((reDto) => {
          this.setBasics({
            category: (reDto.category || 'SALE') as Category,
            description: reDto.description || '',
          });

          // Salva ID principali
          this.detailId.set(reDto.detailId ?? null);
          this.cadastralId.set(reDto.cadastralDataId ?? null);

          // immagini esistenti -> File (come già fai)
          const existingImgs = reDto.images ?? [];
          const files = existingImgs.map((b64, i) =>
            this.base64ToFile(b64, `existing-${i}.jpg`)
          );
          this.images.set(files);

          const detailId = reDto.detailId;
          const cadId = reDto.cadastralDataId;

          const loadDetail$ = detailId
            ? this.det
                .getDetailById$Response({ detailid: detailId })
                .pipe(map((r) => r.body!))
            : EMPTY;

          const loadCad$ = cadId
            ? this.cad
                .getCadastralDataById$Response({ cadastraldataid: cadId })
                .pipe(map((r) => r.body!))
            : EMPTY;

          return loadDetail$.pipe(
            switchMap((detail: any) => {
              const geoId = detail?.geographicalPositionId ?? null;
              const utlId = detail?.utilityId ?? null;

              this.geoId.set(geoId);
              this.utilityId.set(utlId);

              const geo$ = geoId
                ? this.geo
                    .getGeographicalPositionById$Response({
                      geographicalpositionid: geoId,
                    })
                    .pipe(map((r) => r.body!))
                : EMPTY;
              const utl$ = utlId
                ? this.utl
                    .getUtilityById$Response({ utilityid: utlId })
                    .pipe(map((r) => r.body!))
                : EMPTY;

              return from([null]).pipe(
                switchMap(() => geo$),
                map((geo: any) => {
                  if (geo) {
                    this.setPosition({
                      address: geo.address,
                      city: geo.city,
                      municipality: geo.municipality,
                      latitude: geo.latitude,
                      longitude: geo.longitude,
                      radius: geo.radius ?? 0,
                    });
                  }
                  return null;
                }),
                switchMap(() => utl$),
                map((u: any) => {
                  if (u) {
                    this.setUtilities({
                      hasElevator: !!u.hasElevator,
                      hasDoorman: !!u.hasDoorman,
                      hasAirConditioning: !!u.hasAirConditioning,

                      nearPark: !!u.nearPark,
                      nearPublicTransport: !!u.nearPublicTransport,
                      nearSchool: !!u.nearSchool,
                    });
                  }
                  return null;
                }),
                switchMap(() => loadCad$),
                map((cad: any) => {
                  if (cad) {
                    this.setCadastral({
                      price: cad.price,
                      rooms: cad.rooms,
                      floor: cad.floor,
                      energyClass: cad.energyClass,
                      squareMeters: cad.squareMeters,
                    });
                  }
                  return reDto.id!;
                })
              );
            })
          );
        }),
        catchError((e) => {
          this.error.set(
            e?.error?.message || e?.message || 'Caricamento annuncio fallito'
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false))
      )
      .subscribe();
  }

  // ---- utils ----
  private readFilesAsDataURL$(files: File[]): Observable<string> {
    return from(files).pipe(mergeMap((f) => this.readFileAsDataURL$(f)));
  }
  private readFileAsDataURL$(file: File): Observable<string> {
    return new Observable<string>((observer) => {
      const reader = new FileReader();
      reader.onload = () => {
        observer.next(reader.result as string);
        observer.complete();
      };
      reader.onerror = (err) => observer.error(err);
      reader.readAsDataURL(file);
    });
  }
  private dataUrlToBase64(dataUrl: string): string {
    const i = dataUrl.indexOf(',');
    const s = i >= 0 ? dataUrl.slice(i + 1) : dataUrl;
    return s.replace(/\s/g, '');
  }
  private base64ToFile(
    base64: string,
    name: string,
    mime = 'image/jpeg'
  ): File {
    const byteString = atob(base64);
    const bytes = new Uint8Array(byteString.length);
    for (let i = 0; i < byteString.length; i++)
      bytes[i] = byteString.charCodeAt(i);
    const blob = new Blob([bytes], { type: mime });
    return new File([blob], name, { type: mime });
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

  createAd() {
    const basics = this.basics();
    const util = this.utilities();
    const pos = this.position();
    const cad = this.cadastral();
    const imgs = this.images();
    const agentEmail = this.getAgentEmail();

    const realestateId = this.editingId();
    const detId = this.detailId();
    const gpId = this.geoId();
    const utId = this.utilityId();
    const cadId = this.cadastralId();

    if (!basics || !util || !pos || !cad) {
      this.error.set('Compila tutti gli step prima di salvare.');
      return;
    }
    if (!agentEmail) {
      this.error.set(
        "Impossibile ottenere l'email dell'agente. Riesegui il login."
      );
      return;
    }
    if (realestateId == null || Number.isNaN(realestateId)) {
      this.error.set('ID annuncio mancante o non valido.');
      return;
    }
    if (detId == null || gpId == null || utId == null || cadId == null) {
      this.error.set(
        'Struttura annuncio inconsistente: alcuni ID mancanti (detail/geo/utility/cadastral).'
      );
      return;
    }

    const utilityReq = {
      hasElevator: util.hasElevator,
      hasDoorman: util.hasDoorman,
      hasAirConditioning: util.hasAirConditioning,

      nearPark: !!util.nearPark,
      nearPublicTransport: !!util.nearPublicTransport,
      nearSchool: !!util.nearSchool,
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

    // 1) Update Utility
    this.utl
      .updateUtility$Response({ utilityid: utId, body: utilityReq })
      .pipe(
        // 2) Update GeographicalPosition
        switchMap(() =>
          this.geo.updateGeographicalPosition$Response({
            geographicalpositionid: gpId,
            body: gpReq,
          })
        ),
        // 3) Update Detail (ricollega a gp/utility esistenti — idempotente)
        switchMap(() =>
          this.det.updateDetail$Response({
            detailid: detId,
            body: {
              geographicalPositionId: gpId,
              utilityId: utId,
            } as DetailRequest,
          })
        ),
        // 4) Update CadastralData
        switchMap(() =>
          this.cad.updateCadastralData$Response({
            cadastraldataid: cadId,
            body: cadReq,
          })
        ),
        // 5) Ricarica/ricodifica immagini e Update RealEstate
        switchMap(() =>
          this.readFilesAsDataURL$(imgs).pipe(
            map((s) => this.dataUrlToBase64(s)),
            toArray(),
            switchMap((imagesBase64: string[]) =>
              this.re.updateRealEstate$Response({
                realestateid: realestateId,
                body: {
                  detailId: detId,
                  cadastralDataId: cadId,
                  category: basics.category,
                  description: basics.description,
                  estateAgentEmail: agentEmail,
                  images: imagesBase64,
                } as RealEstateRequest,
              })
            )
          )
        ),
        catchError((e) => {
          this.error.set(
            e?.error?.message || e?.message || 'Salvataggio modifiche fallito'
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false))
      )
      .subscribe(() => {
        this.savedSubject.next(realestateId);
        this.router.navigate(['/agent']);
      });
  }
}
