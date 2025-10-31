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
import {
  RealEstateControllerService,
  UtilityControllerService,
} from '../../services/services';
import { UtilityResponse } from '../../services/models/utility-response';

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

  private searchApi = inject(SearchControllerService);
  private detailApi = inject(DetailControllerService);
  private geoApi = inject(GeographicalPositionControllerService);
  private cadApi = inject(CadastralDataControllerService);
  private reApi = inject(RealEstateControllerService);

  private offerApi = inject(OfferControllerService);
  private visitApi = inject(VisitControllerService);
  private utiApi = inject(UtilityControllerService);

  // state
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  re = signal<RealEstateResponse | null>(null);
  det = signal<DetailResponse | null>(null);
  cad = signal<CadastralDataResponse | null>(null);
  geo = signal<GeographicalPositionResponse | null>(null);
  uti = signal<UtilityResponse | null>(null);

  mainImage = signal<string | null>(null);

  myOffers = signal<MyOfferVM[]>([]);
  myOffersLoading = signal<boolean>(false);

  vm = computed<AdVM | null>(() => {
    const re = this.re();
    const det = this.det();
    const cad = this.cad();
    const geo = this.geo();
    const uti = this.uti();
    if (!re || !re.id || !re.detailId) return null;

    const images = re.images ?? [];
    const cover = images[0] ?? null;

    // preferisci det → cad → re
    const price = (re as any)?.price ?? cad?.price ?? null;

    const surface =
      (det as any)?.surface ??
      cad?.squareMeters ??
      (re as any)?.surface ??
      null;

    const rooms =
      (det as any)?.rooms ?? cad?.rooms ?? (re as any)?.rooms ?? null;

    const city = geo?.city ?? null;

    const titleParts = [
      re.category || null,
      city ? `a ${city}` : null,
      surface ? `— ${surface} m²` : null,
    ].filter(Boolean);
    const title = titleParts.length ? titleParts.join(' ') : 'Annuncio';

    const type =
      (re as any)?.type ?? (det as any)?.type ?? (cad as any)?.type ?? null;
    const floor =
      (cad as any)?.floor ?? (det as any)?.floor ?? (re as any)?.floor ?? null;
    const energyClass =
      (cad as any)?.energyClass ??
      (det as any)?.energyClass ??
      (re as any)?.energyClass ??
      null;

    const utilities: string[] = (() => {
      if (!uti) return [];
      const labels: Record<string, string> = {
        hasAirConditioning: 'Aria condizionata',
        hasElevator: 'Ascensore',
        hasDoorman: 'Portineria',
      };
      return Object.entries(uti)
        .filter(([k, v]) => typeof v === 'boolean' && v === true)
        .map(([k]) => labels[k] ?? k);
    })();

    return {
      realEstateId: re.id!,
      detailId: re.detailId!,
      title,
      description: (det as any)?.description ?? re.description ?? null,
      price,
      city,
      surface,
      rooms,
      floor,
      energyClass,
      images,
      coverUrl: cover || undefined,
      agent: { email: (re as any)?.estateAgentEmail ?? null },
      position: {
        latitude: geo?.latitude,
        longitude: geo?.longitude,
        address: geo?.address,
        municipality: geo?.municipality,
      },
      proximityTags: (re as any)?.proximityTags ?? undefined,
      utilities,
    };
  });

  loadByRealEstateId(
    realEstateId: number,
    opts?: { userEmail?: string; category?: 'SALE' | 'RENT' }
  ) {
    if (!realEstateId || Number.isNaN(realEstateId)) {
      this.error.set('ID annuncio non valido.');
      return;
    }
    this.loading.set(true);
    this.error.set(null);

    this.det.set(null);
    this.re.set(null);
    this.cad.set(null);
    this.geo.set(null);
    this.uti.set(null);
    this.mainImage.set(null);

    const userEmail = opts?.userEmail ?? 'guest@public.local';

    this.reApi.getRealEstateById({ realestateid: realEstateId }).subscribe({
      next: (re) => {
        this.re.set(re);
        this.loadMyOffers(userEmail, re.id!);
        const imgs = re.images ?? [];
        this.mainImage.set(imgs[0] ?? null);

        const detailId = re.detailId;
        if (detailId != null) {
          this.detailApi.getDetailById({ detailid: detailId }).subscribe({
            next: (det) => {
              this.det.set(det);

              console.log('[DETAIL]', det);

              const utilityId =
                (det as any)?.utilityId ?? (det as any)?.utility?.id;
              if (utilityId != null) {
                this.utiApi.getUtilityById({ utilityid: utilityId }).subscribe({
                  next: (u) => this.uti.set(u),
                  error: () => {},
                });
              }

              if (det.geographicalPositionId != null) {
                this.geoApi
                  .getGeographicalPositionById({
                    geographicalpositionid: det.geographicalPositionId,
                  })
                  .subscribe({
                    next: (geo) => this.geo.set(geo),
                    error: () => {},
                  });
              }

              if (re.cadastralDataId != null) {
                this.cadApi
                  .getCadastralDataById({ cadastraldataid: re.cadastralDataId })
                  .subscribe({
                    next: (cad) => this.cad.set(cad),
                    error: () => {},
                  });
              }
              this.loading.set(false);
            },
            error: (e) => this.fail(e),
          });
        } else {
          this.loading.set(false);
          this.error.set('Annuncio non trovato.');
        }
      },
      error: (e) => this.fail(e),
    });
  }

  setMain(src: string) {
    this.mainImage.set(src);
  }

  submitOffer(amount: number, category: 'SALE' | 'RENT', userEmail: string) {
    const vm = this.vm();
    if (!vm) return;
    const body: OfferRequest = {
      amount,
      category,
      status: 'PENDING',
      userEmail,
    };
    return this.offerApi.createOffer({ realestateid: vm.realEstateId, body });
  }

  submitVisit(
    date: string,
    time: string,
    category: 'SALE' | 'RENT',
    userEmail: string
  ) {
    const vm = this.vm();
    if (!vm) return;
    const body: VisitRequest = {
      category,
      status: 'PENDING',
      date,
      time,
      userEmail,
    };
    return this.visitApi.createVisit({ realestateid: vm.realEstateId, body });
  }

  private fail(e: unknown) {
    console.error(e);
    this.error.set(
      typeof e === 'string' ? e : 'Errore durante il caricamento.'
    );
    this.loading.set(false);
  }

  private toMyOfferVM(o: any): MyOfferVM {
    return {
      id: o?.id ?? 0,
      amount: o?.amount ?? 0,
      status: (o?.status ?? 'PENDING') as any,
      createdAt: o?.createdAt ?? o?.createdDate ?? null,
    };
  }

  loadMyOffers(userEmail: string, realEstateId: number) {
    if (!userEmail || !realEstateId) {
      this.myOffers.set([]);
      return;
    }

    this.myOffersLoading.set(true);

    this.offerApi
      .listOffersForRealEstate({
        realestateid: realEstateId,
        page: 0,
        size: 100,
      })
      .subscribe({
        next: (list) => {
          const arr = Array.isArray(list) ? list : [];

          const email = userEmail.toLowerCase();
          const mine = arr.filter(
            (o: any) =>
              (o?.realEstateId ?? o?.estateId ?? o?.ad?.id) === realEstateId &&
              (o?.userEmail ?? '').toLowerCase() === email
          );

          this.myOffers.set(mine.map((o) => this.toMyOfferVM(o)));
          this.myOffersLoading.set(false);
        },
        error: (e) => {
          console.error(e);
          this.myOffers.set([]);
          this.myOffersLoading.set(false);
        },
      });
  }
}
