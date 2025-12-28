import { Injectable, signal, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { EMPTY, Subject } from 'rxjs';
import { catchError, finalize, map, switchMap } from 'rxjs/operators';

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
import { BasicsDraft } from '../../interfaces/create-ad/basic-draft';
import { UtilitiesDraft } from '../../interfaces/create-ad/utilities-draft';
import { PositionDraft } from '../../interfaces/create-ad/position-draft';
import { CadastralDraft } from '../../interfaces/create-ad/cadastral-draft';

@Injectable({ providedIn: 'root' })
export class CreateAdFacade {
  private geographicalPositionService = inject(
    GeographicalPositionControllerService,
  );
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
        this.images().length > 0
      ),
  );

  getBasics() {
    return this.basics();
  }

  getUtility() {
    return this.utility();
  }

  getGeographicalPosition() {
    return this.geographicalPosition();
  }

  getCadastralData() {
    return this.cadastralData();
  }

  getImages() {
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

    this.utlilityService
      .createUtility$Response({ body: utilityRequest })
      .pipe(
        map((response): number => {
          const id = this.extractId(response, ['utilityId']);
          if (id == null) throw new Error('Utility creata ma ID non presente');
          return id;
        }),

        switchMap((utilityId: number) =>
          this.geographicalPositionService
            .createGeographicalPosition$Response({
              body: geographicalPositionRequest,
            })
            .pipe(
              map(
                (
                  response,
                ): {
                  utilityId: number;
                  geographicalPositionId: number;
                } => {
                  const geographicalPositionId = this.extractId(response, [
                    'geographicalPositionId',
                  ]);
                  if (geographicalPositionId == null)
                    throw new Error(
                      'GeographicalPosition creata ma ID non presente',
                    );
                  return { utilityId, geographicalPositionId };
                },
              ),
            ),
        ),

        switchMap(({ utilityId, geographicalPositionId }) =>
          this.detailService
            .createDetail$Response({
              body: {
                geographicalPositionId,
                utilityId,
              } as DetailRequest,
            })
            .pipe(
              map(
                (
                  response,
                ): {
                  detailId: number;
                } => {
                  const detailId = this.extractId(response, ['detailId']);
                  if (detailId == null)
                    throw new Error('Detail creato ma ID non presente');
                  return { detailId };
                },
              ),
            ),
        ),

        switchMap(({ detailId }) =>
          this.cadastralDataService
            .createCadastralData$Response({ body: cadastralDataRequest })
            .pipe(
              map(
                (
                  response,
                ): {
                  detailId: number;
                  cadastralId: number;
                } => {
                  const cadastralId = this.extractId(response, [
                    'cadastralDataId',
                  ]);
                  if (cadastralId == null)
                    throw new Error('Cadastral creato ma ID non presente');
                  return { detailId, cadastralId };
                },
              ),
            ),
        ),

        switchMap(({ detailId, cadastralId }) => {
          const realEstateData: RealEstateRequest = {
            category: basics.category,
            description: basics.description,
            detailId,
            cadastralDataId: cadastralId,
          };

          return this.realEstateService.createRealEstate$Response({
            body: {
              data: realEstateData,
              images: imgs as Blob[],
            },
          });
        }),

        map((response): number => {
          const realEstateId = this.extractId(response, ['realEstateId']);
          if (realEstateId == null)
            throw new Error('Annuncio creato ma ID non presente');
          return realEstateId;
        }),

        catchError((error) => {
          this.error.set(
            error?.error?.message ||
              error?.message ||
              'Creazione annuncio fallita',
          );
          return EMPTY;
        }),
        finalize(() => this.loading.set(false)),
      )
      .subscribe((realEstateId: number) => {
        this.publishedSubject.next(realEstateId);

        this.clearSavedData();

        this.routerService.navigateByUrl('/');
      });
  }

  clearSavedData() {
    this.basics.set(null);
    this.utility.set(null);
    this.geographicalPosition.set(null);
    this.cadastralData.set(null);
    this.images.set([]);
  }

  // ---- utils ----
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
