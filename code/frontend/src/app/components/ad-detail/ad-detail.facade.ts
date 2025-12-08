import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';

import { SearchControllerService } from '../../services/services/search-controller.service';
import { DetailControllerService } from '../../services/services/detail-controller.service';
import { GeographicalPositionControllerService } from '../../services/services/geographical-position-controller.service';
import { CadastralDataControllerService } from '../../services/services/cadastral-data-controller.service';
import { OfferControllerService } from '../../services/services/offer-controller.service';
import { VisitControllerService } from '../../services/services/visit-controller.service';

import { RealEstateResponse } from '../../services/models/real-estate-response';
import { DetailResponse } from '../../services/models/detail-response';
import { GeographicalPositionResponse } from '../../services/models/geographical-position-response';
import { CadastralDataResponse } from '../../services/models/cadastral-data-response';
import { OfferRequest } from '../../services/models/offer-request';
import { VisitRequest } from '../../services/models/visit-request';
import { OfferResponse } from '../../services/models/offer-response';
import { PageOfferResponse } from '../../services/models/page-offer-response';
import {
  RealEstateControllerService,
  UtilityControllerService,
} from '../../services/services';
import { UtilityResponse } from '../../services/models/utility-response';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../manual_services/auth/auth.service';

export type AdVM = {
  realEstateId: number;
  detailId: number;
  title: string;
  description?: string | null;
  price?: number | null;
  city?: string | null;
  surface?: number | null;
  rooms?: number | null;

  // mantenuti
  type?: string | null;
  floor?: number | null;
  energyClass?: string | null;

  images: string[];
  coverUrl?: string;
  agent?: { email?: string | null };
  position?: {
    latitude?: number | null;
    longitude?: number | null;
    address?: string | null;
    municipality?: string | null;
  };

  proximityTags?: ProximityTag[];
  utilities?: string[];
};

export type MyOfferVM = {
  id: number;
  amount: number;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED' | 'COUNTERED' | string;
  createdAt?: string | null;
};

export enum ProximityTag {
  NEAR_SCHOOLS = 'NEAR_SCHOOLS',
  NEAR_PARKS = 'NEAR_PARKS',
  NEAR_PUBLIC_TRANSPORT = 'NEAR_PUBLIC_TRANSPORT',
}

@Injectable({ providedIn: 'root' })
export class AdDetailFacade {
  private router = inject(Router);

  private searchService = inject(SearchControllerService);
  private detailService = inject(DetailControllerService);
  private geographicalPositionService = inject(
    GeographicalPositionControllerService,
  );
  private cadastralDataService = inject(CadastralDataControllerService);
  private realEstateService = inject(RealEstateControllerService);

  private offerService = inject(OfferControllerService);
  private visitService = inject(VisitControllerService);
  private utilityService = inject(UtilityControllerService);

  // state
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  realEstateResponse = signal<RealEstateResponse | null>(null);
  detailResponse = signal<DetailResponse | null>(null);
  cadastralDataResponse = signal<CadastralDataResponse | null>(null);
  geographicalPositionResponse = signal<GeographicalPositionResponse | null>(
    null,
  );
  utilityResponse = signal<UtilityResponse | null>(null);

  mainImage = signal<string | null>(null);

  myOffers = signal<MyOfferVM[]>([]);
  myOffersLoading = signal<boolean>(false);

  vm = computed<AdVM | null>(() => {
    const realEstateResponse = this.realEstateResponse();
    const detailResponse = this.detailResponse();
    const cadastralDataResponse = this.cadastralDataResponse();
    const geographicalPositionResponse = this.geographicalPositionResponse();
    const utilityResponse = this.utilityResponse();
    if (
      !realEstateResponse ||
      !realEstateResponse.id ||
      !realEstateResponse.detailId
    )
      return null;

    const images = realEstateResponse.images ?? [];
    const cover = images[0] ?? null;

    // preferisci det → cad → re
    const price =
      (realEstateResponse as any)?.price ??
      cadastralDataResponse?.price ??
      null;

    const surface =
      (detailResponse as any)?.surface ??
      cadastralDataResponse?.squareMeters ??
      (realEstateResponse as any)?.surface ??
      null;

    const rooms =
      (detailResponse as any)?.rooms ??
      cadastralDataResponse?.rooms ??
      (realEstateResponse as any)?.rooms ??
      null;

    const city = geographicalPositionResponse?.city ?? null;

    const titleParts = [
      realEstateResponse.category || null,
      city ? `a ${city}` : null,
      surface ? `— ${surface} m²` : null,
    ].filter(Boolean);
    const title = titleParts.length ? titleParts.join(' ') : 'Annuncio';

    const type =
      (realEstateResponse as any)?.type ??
      (detailResponse as any)?.type ??
      (cadastralDataResponse as any)?.type ??
      null;
    const floor =
      (cadastralDataResponse as any)?.floor ??
      (detailResponse as any)?.floor ??
      (realEstateResponse as any)?.floor ??
      null;
    const energyClass =
      (cadastralDataResponse as any)?.energyClass ??
      (detailResponse as any)?.energyClass ??
      (realEstateResponse as any)?.energyClass ??
      null;

    const utilities: string[] = (() => {
      if (!utilityResponse) return [];
      const labels: Record<string, string> = {
        hasAirConditioning: 'Aria condizionata',
        hasElevator: 'Ascensore',
        hasDoorman: 'Portineria',
      };
      return Object.entries(utilityResponse)
        .filter(([k, v]) => typeof v === 'boolean' && v === true)
        .map(([k]) => labels[k] ?? k);
    })();

    return {
      realEstateId: realEstateResponse.id!,
      detailId: realEstateResponse.detailId!,
      title,
      description:
        (detailResponse as any)?.description ??
        realEstateResponse.description ??
        null,
      price,
      city,
      surface,
      rooms,
      floor,
      energyClass,
      images,
      coverUrl: cover || undefined,
      agent: { email: (realEstateResponse as any)?.estateAgentEmail ?? null },
      position: {
        latitude: geographicalPositionResponse?.latitude,
        longitude: geographicalPositionResponse?.longitude,
        address: geographicalPositionResponse?.address,
        municipality: geographicalPositionResponse?.municipality,
      },
      proximityTags: (realEstateResponse as any)?.proximityTags ?? undefined,
      utilities,
    };
  });

  loadByRealEstateId(
    realEstateId: number,
    opts?: { userEmail?: string; category?: 'SALE' | 'RENT' },
  ) {
    if (!realEstateId || Number.isNaN(realEstateId)) {
      this.error.set('ID annuncio non valido.');
      return;
    }
    this.loading.set(true);
    this.error.set(null);

    this.detailResponse.set(null);
    this.realEstateResponse.set(null);
    this.cadastralDataResponse.set(null);
    this.geographicalPositionResponse.set(null);
    this.utilityResponse.set(null);
    this.mainImage.set(null);

    this.realEstateService
      .getRealEstateById({ realestateid: realEstateId })
      .subscribe({
        next: (realEstate) => {
          this.realEstateResponse.set(realEstate);
          this.loadMyOffers(realEstate.id!);
          const imgs = realEstate.images ?? [];
          const mainImageUrl = `${environment.apiBaseUrl}${imgs[0]}`;
          this.mainImage.set(mainImageUrl);

          const detailId = realEstate.detailId;
          if (detailId != null) {
            this.detailService.getDetailById({ detailid: detailId }).subscribe({
              next: (detail) => {
                this.detailResponse.set(detail);

                console.log('[DETAIL]', detail);

                const utilityId =
                  (detail as any)?.utilityId ?? (detail as any)?.utility?.id;
                if (utilityId != null) {
                  this.utilityService
                    .getUtilityById({ utilityid: utilityId })
                    .subscribe({
                      next: (utility) => this.utilityResponse.set(utility),
                      error: () => {},
                    });
                }

                if (detail.geographicalPositionId != null) {
                  this.geographicalPositionService
                    .getGeographicalPositionById({
                      geographicalpositionid: detail.geographicalPositionId,
                    })
                    .subscribe({
                      next: (geographicalPosition) =>
                        this.geographicalPositionResponse.set(
                          geographicalPosition,
                        ),
                      error: () => {},
                    });
                }

                if (realEstate.cadastralDataId != null) {
                  this.cadastralDataService
                    .getCadastralDataById({
                      cadastraldataid: realEstate.cadastralDataId,
                    })
                    .subscribe({
                      next: (cadastralData) =>
                        this.cadastralDataResponse.set(cadastralData),
                      error: () => {},
                    });
                }
                this.loading.set(false);
              },
              error: (error) => this.fail(error),
            });
          } else {
            this.loading.set(false);
            this.error.set('Annuncio non trovato.');
          }
        },
        error: (error) => this.fail(error),
      });
  }

  setMain(src: string) {
    this.mainImage.set(src);
  }

  submitOffer(amount: number, category: 'SALE' | 'RENT') {
    const vm = this.vm();
    if (!vm) return;
    const body: OfferRequest = {
      amount,
      category,
      status: 'PENDING',
    };
    return this.offerService.createOffer({
      realestateid: vm.realEstateId,
      body,
    });
  }

  submitVisit(date: string, time: string, category: 'SALE' | 'RENT') {
    const vm = this.vm();
    if (!vm) return;

    const body: VisitRequest = {
      category,
      status: 'PENDING',
      date,
      time,
    };
    return this.visitService.createVisit({
      realestateid: vm.realEstateId!,
      body,
    });
  }

  private fail(error: unknown) {
    console.error(error);
    this.error.set(
      typeof error === 'string' ? error : 'Errore durante il caricamento.',
    );
    this.loading.set(false);
  }

  private toMyOfferVM(offerResponse: OfferResponse): MyOfferVM {
    return {
      id: (offerResponse as any)?.id ?? 0,
      amount: (offerResponse as any)?.amount ?? 0,
      status: ((offerResponse as any)?.status ?? 'PENDING') as any,
      createdAt:
        (offerResponse as any)?.createdAt ??
        (offerResponse as any)?.createdDate ??
        null,
    };
  }

  loadMyOffers(realEstateId: number) {
    if (!realEstateId) {
      this.myOffers.set([]);
      return;
    }

    this.myOffersLoading.set(true);

    this.offerService
      .getOffers({
        realestateid: realEstateId,
        page: 0,
        size: 100,
      })
      .subscribe({
        next: (page: PageOfferResponse) => {
          const offers: OfferResponse[] = Array.isArray(page?.content)
            ? (page.content as OfferResponse[])
            : [];

          this.myOffers.set(
            offers.map((offerResponse: OfferResponse) =>
              this.toMyOfferVM(offerResponse),
            ),
          );
          this.myOffersLoading.set(false);
        },
        error: (error) => {
          console.error(error);
          this.myOffers.set([]);
          this.myOffersLoading.set(false);
        },
      });
  }
}
