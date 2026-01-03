import { Injectable, signal, computed, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EMPTY, from, of, Subject, throwError } from 'rxjs';
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
import { BasicsDraft } from '../../interfaces/create-ad/basic-draft';
import { UtilitiesDraft } from '../../interfaces/create-ad/utilities-draft';
import { PositionDraft } from '../../interfaces/create-ad/position-draft';
import { CadastralDraft } from '../../interfaces/create-ad/cadastral-draft';
import { Category } from '../../interfaces/category';

@Injectable()
export class EditAdFacade {
  // ID degli oggetti già esistenti
  private detailId = signal<number | null>(null);
  private geographicalPositionId = signal<number | null>(null);
  private utilityId = signal<number | null>(null);
  private cadastralDataId = signal<number | null>(null);

  private geographicalPositionService = inject(
    GeographicalPositionControllerService,
  );
  private cadsatralDataService = inject(CadastralDataControllerService);
  private detailService = inject(DetailControllerService);
  private utilityService = inject(UtilityControllerService);
  private realEstateService = inject(RealEstateControllerService);
  private routerService = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  readonly mode = 'edit' as const;

  basics = signal<BasicsDraft | null>(null);
  utility = signal<UtilitiesDraft | null>(null);
  geographicalPosition = signal<PositionDraft | null>(null);
  cadastralData = signal<CadastralDraft | null>(null);
  images = signal<File[]>([]);
  existingImageUrls = signal<string[]>([]);

  loading = signal(false);
  error = signal<string | null>(null);

  private savedSubject = new Subject<number>();
  saved$ = this.savedSubject.asObservable();
  published$ = this.saved$;

  private editingId = signal<number | null>(null);

  allValid = computed(() => {
    const hasAnyImages =
      this.images().length > 0 || this.existingImageUrls().length > 0;

    return !!(
      this.basics() &&
      this.utility() &&
      this.geographicalPosition() &&
      this.cadastralData() &&
      hasAnyImages
    );
  });

  getBasics(): BasicsDraft | null {
    return this.basics();
  }

  getUtility(): UtilitiesDraft | null {
    return this.utility();
  }

  getGeographicalPosition(): PositionDraft | null {
    return this.geographicalPosition();
  }

  getCadastralData(): CadastralDraft | null {
    return this.cadastralData();
  }

  getImages(): File[] {
    return this.images();
  }

  setBasics(basicsDraft: BasicsDraft) {
    this.basics.set(basicsDraft);
  }

  setUtility(utilityDraft: UtilitiesDraft) {
    this.utility.set(utilityDraft);
  }

  setGeographicalPosition(geographicalPositionDraft: PositionDraft) {
    this.geographicalPosition.set(geographicalPositionDraft);
  }

  setCadastralData(cadastralDataDraft: CadastralDraft) {
    this.cadastralData.set(cadastralDataDraft);
  }

  setUtilities(utilityDraft: UtilitiesDraft) {
    this.setUtility(utilityDraft);
  }

  setPosition(geographicalPositionDraft: PositionDraft) {
    this.setGeographicalPosition(geographicalPositionDraft);
  }

  setCadastral(cadastralDraft: CadastralDraft) {
    this.setCadastralData(cadastralDraft);
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
          (realEstateResponse) => realEstateResponse.body as RealEstateResponse,
        ),
        switchMap((realEstateDto) => {
          this.setBasics({
            category: (realEstateDto.category || 'SALE') as Category,
            description: realEstateDto.description || '',
          });

          this.detailId.set(realEstateDto.detailId ?? null);
          this.cadastralDataId.set(realEstateDto.cadastralDataId ?? null);

          this.existingImageUrls.set((realEstateDto.images ?? []).filter(Boolean) as string[]);
          this.images.set([]);

          const detailId = realEstateDto.detailId;
          const cadastralDataId = realEstateDto.cadastralDataId;

          const loadDetail$ = detailId
            ? this.detailService
                .getDetailById$Response({ detailid: detailId })
                .pipe(map((detailResponse) => detailResponse.body!))
            : EMPTY;

          const loadCadastralData$ = cadastralDataId
            ? this.cadsatralDataService
                .getCadastralDataById$Response({
                  cadastraldataid: cadastralDataId,
                })
                .pipe(
                  map((cadastralDataResponse) => cadastralDataResponse.body!),
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
                          geographicalPositionResponse.body!,
                      ),
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
                      region: geographicalPosition.region,
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
                }),
              );
            }),
          );
        }),
        catchError((error) => {
          this.error.set(
            error?.error?.message ||
              error?.message ||
              'Caricamento annuncio fallito',
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false)),
      )
      .subscribe();
  }

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
        'Struttura annuncio inconsistente: alcuni ID mancanti (detail/geo/utility/cadastral).',
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
      region: geographicalPosition.region,
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
      .updateUtility$Response({ utilityid: utilityId, body: utilityRequest })
      .pipe(
        catchError((err) => {
          if (err?.status === 404) {
            console.warn('Utility non trovata, salto updateUtility', err);
            return of(null);
          }
          return throwError(() => err);
        }),
        switchMap(() =>
          this.geographicalPositionService.updateGeographicalPosition$Response({
            geographicalpositionid: geographicalPositionId,
            body: geographicalPositionRequest,
          }),
        ),

        switchMap(() =>
          this.detailService.updateDetail$Response({
            detailid: detailId,
            body: {
              geographicalPositionId,
              utilityId,
            } as DetailRequest,
          }),
        ),
        switchMap(() =>
          this.cadsatralDataService.updateCadastralData$Response({
            cadastraldataid: cadastralDataId,
            body: cadastralDataRequest,
          }),
        ),
        switchMap(() => {
          const realEstateData: RealEstateRequest = {
            category: basics.category,
            description: basics.description,
            detailId,
            cadastralDataId,
          };

          const body: any = {
            data: realEstateData,
            images: imgs as Blob[], 
            existingImages: JSON.stringify(this.existingImageUrls()),
          };
          
          return this.realEstateService.updateRealEstate$Response({
            realestateid: realestateId,
            body,
          });
        }),

        catchError((error) => {
          this.error.set(
            error?.error?.message ||
              error?.message ||
              'Salvataggio modifiche fallito',
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false)),
      )
      .subscribe(() => {
        this.savedSubject.next(realestateId);
        this.routerService.navigate(['/agent']);
      });
  }

  // ---- utils ----
  private base64ToFile(
    base64Like: string,
    name: string,
    mime = 'image/jpeg',
  ): File | null {
    try {
      if (!base64Like) return null;

      let base64 = base64Like.trim();

      const dataUrlMatch = /^data:(.*?);base64,(.*)$/.exec(base64);
      if (dataUrlMatch) {
        mime = dataUrlMatch[1] || mime;
        base64 = dataUrlMatch[2];
      }

      base64 = base64.replace(/[\r\n\s]/g, '');

      const pad = base64.length % 4;
      if (pad) {
        base64 = base64 + '='.repeat(4 - pad);
      }

      const byteString = atob(base64);
      const bytes = new Uint8Array(byteString.length);
      for (let i = 0; i < byteString.length; i++) {
        bytes[i] = byteString.charCodeAt(i);
      }

      const blob = new Blob([bytes], { type: mime });
      return new File([blob], name, { type: mime });
    } catch (e) {
      console.warn('Immagine non in base64 valido, la salto:', base64Like, e);
      return null;
    }
  }

  clearSavedData(){
    this.basics.set(null);
    this.utility.set(null);
    this.geographicalPosition.set(null);
    this.cadastralData.set(null);
    this.images.set([]);
    this.existingImageUrls.set([]);

    this.error.set(null);

    this.detailId.set(null);
    this.geographicalPositionId.set(null);
    this.utilityId.set(null);
    this.cadastralDataId.set(null);
    this.editingId.set(null);
  }
}
