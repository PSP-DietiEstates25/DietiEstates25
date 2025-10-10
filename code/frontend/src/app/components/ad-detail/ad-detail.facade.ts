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
};

@Injectable({ providedIn: 'root' })
export class AdDetailFacade {
  private router = inject(Router);

  private searchApi = inject(SearchControllerService);
  private detailApi = inject(DetailControllerService);
  private geoApi = inject(GeographicalPositionControllerService);
  private cadApi = inject(CadastralDataControllerService);
  private offerApi = inject(OfferControllerService);
  private visitApi = inject(VisitControllerService);

  // state
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  re = signal<RealEstateResponse | null>(null);
  det = signal<DetailResponse | null>(null);
  cad = signal<CadastralDataResponse | null>(null);
  geo = signal<GeographicalPositionResponse | null>(null);

  mainImage = signal<string | null>(null);

  vm = computed<AdVM | null>(() => {
    const re = this.re();
    const det = this.det();
    const cad = this.cad();
    const geo = this.geo();
    if (!re || !re.id || !re.detailId) return null;

    const images = re.images ?? [];
    const cover = images[0];

    const city = geo?.city ?? null;
    const surface = cad?.squareMeters ?? null;
    const titleParts = [
      re.category || null,
      city ? `a ${city}` : null,
      surface ? `— ${surface} m²` : null,
    ].filter(Boolean);
    const title = titleParts.length ? titleParts.join(' ') : 'Annuncio';

    // prova a pescare dai vari DTO se presenti (sostituisci con campi tipizzati se li hai)
    const type =
      (re as any)?.type ?? (det as any)?.type ?? (cad as any)?.type ?? null;
    const floor =
      (cad as any)?.floor ?? (det as any)?.floor ?? (re as any)?.floor ?? null;
    const energyClass =
      (cad as any)?.energyClass ??
      (det as any)?.energyClass ??
      (re as any)?.energyClass ??
      null;

    return {
      realEstateId: re.id!,
      detailId: re.detailId!,
      title,
      description: re.description,
      price: cad?.price ?? null,
      city,
      surface,
      rooms: cad?.rooms ?? null,

      type,
      floor,
      energyClass,

      images,
      coverUrl: cover,
      agent: { email: re.estateAgentEmail },
      position: {
        latitude: geo?.latitude,
        longitude: geo?.longitude,
        address: geo?.address,
        municipality: geo?.municipality,
      },
    };
  });

  /** Carica tutto a partire dal detailId. */
  loadByDetailId(
    detailId: number,
    opts?: { userEmail?: string; category?: 'SALE' | 'RENT' }
  ) {
    if (!detailId || Number.isNaN(detailId)) {
      this.error.set('ID annuncio non valido.');
      return;
    }
    this.loading.set(true);
    this.error.set(null);

    this.det.set(null);
    this.re.set(null);
    this.cad.set(null);
    this.geo.set(null);
    this.mainImage.set(null);

    // 1) detail
    this.detailApi.getDetailById({ detailid: detailId }).subscribe({
      next: (det) => {
        this.det.set(det);

        // 2) search real estate by detailId (fallback SALE→RENT se non passi category)
        const baseReq = {
          detailId,
          page: 1,
          size: 1,
          userEmail: opts?.userEmail ?? 'guest@public.local',
        };

        const doAfterRE = (re: RealEstateResponse) => {
          this.re.set(re);
          const imgs = re.images ?? [];
          this.mainImage.set(imgs[0] ?? null);

          if (re.cadastralDataId != null) {
            this.cadApi
              .getCadastralDataById({ cadastraldataid: re.cadastralDataId })
              .subscribe({
                next: (c) => this.cad.set(c),
                error: () => {},
                complete: () => this.loading.set(false),
              });
          } else {
            this.loading.set(false);
          }
        };

        const searchOnce = (category: 'SALE' | 'RENT', onEmpty: () => void) => {
          this.searchApi
            .createSearch({ body: { ...baseReq, category } as any })
            .subscribe({
              next: (list) => (list?.length ? doAfterRE(list[0]) : onEmpty()),
              error: (e) => this.fail(e),
            });
        };

        if (opts?.category) {
          searchOnce(opts.category, () => this.fail('Annuncio non trovato.'));
        } else {
          // fallback SALE -> RENT
          searchOnce('SALE', () =>
            searchOnce('RENT', () => this.fail('Annuncio non trovato.'))
          );
        }

        // 3) geo opzionale
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
      },
      error: (e) => this.fail(e),
    });
  }

  setMain(src: string) {
    this.mainImage.set(src);
  }

  /** Helper per inviare un’offerta */
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

  /** Helper per prenotare una visita */
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
}
