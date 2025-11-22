import { Injectable, signal, computed, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EMPTY, from, Observable, Subject } from 'rxjs';
import { catchError, finalize, map, switchMap } from 'rxjs/operators';

import {
  GeographicalPositionControllerService,
  CadastralDataControllerService,
  DetailControllerService,
  UtilityControllerService,
  RealEstateControllerService,
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
export interface UtilityDraft {
  hasElevator: boolean;
  hasDoorman: boolean;
  hasAirConditioning: boolean;

  nearPark: boolean;
  nearPublicTransport: boolean;
  nearSchool: boolean;
}
export interface GeographicalPositionDraft {
  address: string;
  city: string;
  municipality: string;
  latitude: number;
  longitude: number;
  radius?: number;
}
export interface CadastralDataDraft {
  price: number;
  rooms: number;
  floor: number;
  energyClass: 'A4' | 'A3' | 'A2' | 'A1' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G';
  squareMeters: number;
}

@Injectable()
export class EditAdFacade {
  // ID degli oggetti già esistenti
  private detailId = signal<number | null>(null);
  private geographicalPositionId = signal<number | null>(null);
  private utilityId = signal<number | null>(null);
  private cadastralDataId = signal<number | null>(null);

  private geographicalPositionService = inject(
    GeographicalPositionControllerService
  );
  private cadsatralDataService = inject(CadastralDataControllerService);
  private detailService = inject(DetailControllerService);
  private utilityService = inject(UtilityControllerService);
  private realEstateService = inject(RealEstateControllerService);
  private routerService = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  readonly mode = 'edit' as const;

  basics = signal<BasicsDraft | null>(null);
  utility = signal<UtilityDraft | null>(null);
  geographicalPosition = signal<GeographicalPositionDraft | null>(null);
  cadastralData = signal<CadastralDataDraft | null>(null);
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
        this.utility() &&
        this.geographicalPosition() &&
        this.cadastralData() &&
        this.images().length > 0
      )
  );

  getBasics(): BasicsDraft | null {
    return this.basics();
  }

  getUtility(): UtilityDraft | null {
    return this.utility();
  }

  getGeographicalPosition(): GeographicalPositionDraft | null {
    return this.geographicalPosition();
  }

  getCadastralData(): CadastralDataDraft | null {
    return this.cadastralData();
  }

  getImages(): File[] {
    return this.images();
  }

  setBasics(basicsDraft: BasicsDraft) {
    this.basics.set(basicsDraft);
  }

  setUtility(utilityDraft: UtilityDraft) {
    this.utility.set(utilityDraft);
  }

  setGeographicalPosition(
    geographicalPositionDraft: GeographicalPositionDraft
  ) {
    this.geographicalPosition.set(geographicalPositionDraft);
  }

  setCadastralData(cadastralDataDraft: CadastralDataDraft) {
    this.cadastralData.set(cadastralDataDraft);
  }

  removeImage(index: number) {
    this.images.update((array) => array.filter((_, idx) => idx !== index));
  }

  setImages(files: File[]) {
    this.images.set(files ?? []);
  }

  addImages(files: File[]) {
    this.images.set([...(this.images() ?? []), ...(files ?? [])]);
  }

  /**
   * Carica l'annuncio esistente, popola segnali e ID.
   */
  load(realEstateId: number) {
    if (Number.isNaN(realEstateId)) {
      const realEstateIdParam =
        this.activatedRoute.snapshot.paramMap.get('realestateId');
      realEstateId = realEstateIdParam ? Number(realEstateIdParam) : NaN;
    }

    if (Number.isNaN(realEstateId)) {
      this.error.set('ID annuncio non valido nell’URL.');
      return;
    }

    this.editingId.set(realEstateId);
    this.loading.set(true);
    this.error.set(null);

    this.realEstateService
      .getRealEstateById$Response({ realestateid: realEstateId })
      .pipe(
        map(
          (realEstateResponse) => realEstateResponse.body as RealEstateResponse
        ),
        switchMap((realEstateDto) => {
          this.setBasics({
            category: (realEstateDto.category || 'SALE') as Category,
            description: realEstateDto.description || '',
          });

          this.detailId.set(realEstateDto.detailId ?? null);
          this.cadastralDataId.set(realEstateDto.cadastralDataId ?? null);

          // immagini esistenti -> File (come prima)
          const existingImgs = realEstateDto.images ?? [];
          const files = existingImgs.map((b64, index) =>
            this.base64ToFile(b64, `existing-${index}.jpg`)
          );
          this.images.set(files);

          const detailId = realEstateDto.detailId;
          const cadastralDataId = realEstateDto.cadastralDataId;

          const loadDetail$ = detailId
            ? this.detailService
                .getDetailById$Response({ detailid: detailId })
                .pipe((r) =>
                  r.pipe(map((detailResponse) => detailResponse.body!))
                )
            : EMPTY;

          const loadCadastralData$ = cadastralDataId
            ? this.cadsatralDataService
                .getCadastralDataById$Response({
                  cadastraldataid: cadastralDataId,
                })
                .pipe(
                  map((cadastralDataResponse) => cadastralDataResponse.body!)
                )
            : EMPTY;

          return loadDetail$.pipe(
            switchMap((detail: any) => {
              const geographicalPositionId =
                detail?.geographicalPositionId ?? null;
              const utilityId = detail?.utilityId ?? null;

              this.geographicalPositionId.set(geographicalPositionId);
              this.utilityId.set(utilityId);

              const geographicalPosition$ = geographicalPositionId
                ? this.geographicalPositionService
                    .getGeographicalPositionById$Response({
                      geographicalpositionid: geographicalPositionId,
                    })
                    .pipe(
                      map(
                        (geographicalPositionResponse) =>
                          geographicalPositionResponse.body!
                      )
                    )
                : EMPTY;

              const utility$ = utilityId
                ? this.utilityService
                    .getUtilityById$Response({ utilityid: utilityId })
                    .pipe(map((utilityResponse) => utilityResponse.body!))
                : EMPTY;

              return from([null]).pipe(
                switchMap(() => geographicalPosition$),
                map((geographicalPosition: any) => {
                  if (geographicalPosition) {
                    this.setGeographicalPosition({
                      address: geographicalPosition.address,
                      city: geographicalPosition.city,
                      municipality: geographicalPosition.municipality,
                      latitude: geographicalPosition.latitude,
                      longitude: geographicalPosition.longitude,
                      radius: geographicalPosition.radius ?? 0,
                    });
                  }
                  return null;
                }),
                switchMap(() => utility$),
                map((utility: any) => {
                  if (utility) {
                    this.setUtility({
                      hasElevator: !!utility.hasElevator,
                      hasDoorman: !!utility.hasDoorman,
                      hasAirConditioning: !!utility.hasAirConditioning,

                      nearPark: !!utility.nearPark,
                      nearPublicTransport: !!utility.nearPublicTransport,
                      nearSchool: !!utility.nearSchool,
                    });
                  }
                  return null;
                }),
                switchMap(() => loadCadastralData$),
                map((cadastralData: any) => {
                  if (cadastralData) {
                    this.setCadastralData({
                      price: cadastralData.price,
                      rooms: cadastralData.rooms,
                      floor: cadastralData.floor,
                      energyClass: cadastralData.energyClass,
                      squareMeters: cadastralData.squareMeters,
                    });
                  }
                  return realEstateDto.id!;
                })
              );
            })
          );
        }),
        catchError((error) => {
          this.error.set(
            error?.error?.message ||
              error?.message ||
              'Caricamento annuncio fallito'
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false))
      )
      .subscribe();
  }

  /**
   * UPDATE FLOW:
   * update Utility -> update Geo -> update Detail -> update CadastralData -> update RealEstate (multipart)
   */
  createAd() {
    const basics = this.basics();
    const utility = this.utility();
    const geographicalPosition = this.geographicalPosition();
    const cadastralData = this.cadastralData();
    const imgs = this.images();

    const realestateId = this.editingId();
    const detailId = this.detailId();
    const geographicalPositionId = this.geographicalPositionId();
    const utilityId = this.utilityId();
    const cadastralDataId = this.cadastralDataId();

    if (!basics || !utility || !geographicalPosition || !cadastralData) {
      this.error.set('Compila tutti gli step prima di salvare.');
      return;
    }

    if (realestateId == null || Number.isNaN(realestateId)) {
      this.error.set('ID annuncio mancante o non valido.');
      return;
    }

    if (
      detailId == null ||
      geographicalPositionId == null ||
      utilityId == null ||
      cadastralDataId == null
    ) {
      this.error.set(
        'Struttura annuncio inconsistente: alcuni ID mancanti (detail/geo/utility/cadastral).'
      );
      return;
    }

    const utilityRequest: UtilityRequest = {
      hasElevator: utility.hasElevator,
      hasDoorman: utility.hasDoorman,
      hasAirConditioning: utility.hasAirConditioning,

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

    this.utilityService
      .updateUtility$Response({
        utilityid: utilityId,
        body: utilityRequest,
      })
      .pipe(
        switchMap(() =>
          this.geographicalPositionService.updateGeographicalPosition$Response({
            geographicalpositionid: geographicalPositionId,
            body: geographicalPositionRequest,
          })
        ),
        switchMap(() =>
          this.detailService.updateDetail$Response({
            detailid: detailId,
            body: {
              geographicalPositionId,
              utilityId,
            } as DetailRequest,
          })
        ),
        switchMap(() =>
          this.cadsatralDataService.updateCadastralData$Response({
            cadastraldataid: cadastralDataId,
            body: cadastralDataRequest,
          })
        ),
        // QUI: niente più base64. Inviamo multipart (data + images)
        switchMap(() => {
          const realEstateData: RealEstateRequest = {
            category: basics.category,
            description: basics.description,
            detailId,
            cadastralDataId,
          };

          return this.realEstateService.updateRealEstate$Response({
            realestateid: realestateId,
            body: {
              data: realEstateData,
              images: imgs as Blob[],
            },
          });
        }),
        catchError((error) => {
          this.error.set(
            error?.error?.message ||
              error?.message ||
              'Salvataggio modifiche fallito'
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false))
      )
      .subscribe(() => {
        this.savedSubject.next(realestateId);
        this.routerService.navigate(['/agent']);
      });
  }

  // ---- utils ----
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
}
