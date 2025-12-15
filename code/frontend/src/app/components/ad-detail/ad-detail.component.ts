import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AdDetailFacade } from './ad-detail.facade';
import { OfferFormComponent } from '../offer/offer-form.component';
import { VisitFormComponent } from '../visit/visit-form.component';
import { DecimalPipe, DatePipe } from '@angular/common';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../manual_services/auth/auth.service';

import { environment } from '../../../environments/environment';
import { AirConditioningIconComponent } from '../../shared/icons/air-conditioning-icon/air-conditioning-icon.component';
import { ElevatorIconComponent } from '../../shared/icons/elevator-icon/elevator-icon.component';
import { DoormanIconComponent } from '../../shared/icons/doorman-icon/doorman-icon.component';
import { NearParkIconComponent } from '../../shared/icons/near-park-icon/near-park-icon.component';
import { NearPublicTransportIconComponent } from '../../shared/icons/near-public-transport-icon/near-public-transport-icon.component';
import { NearSchoolIconComponent } from '../../shared/icons/near-school-icon/near-school-icon.component';

const isHttp = (s: string) => /^https?:\/\//i.test(s);
const isData = (s: string) => /^data:/i.test(s);
const looksJpeg = (b64: string) => b64.startsWith('/9j/');
const looksPng = (b64: string) => b64.startsWith('iVBOR');

@Component({
  selector: 'app-ad-detail',
  standalone: true,
  imports: [
    AirConditioningIconComponent,
    ElevatorIconComponent,
    DoormanIconComponent,
    NearParkIconComponent,
    NearPublicTransportIconComponent,
    NearSchoolIconComponent,
    OfferFormComponent,
    VisitFormComponent,
    DecimalPipe,
    NavbarComponent,
    DatePipe,
  ],
  templateUrl: './ad-detail.component.html',
})
export class AdDetailComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);
  private facade = inject(AdDetailFacade);
  private http = inject(HttpClient);

  loading = this.facade.loading;
  error = this.facade.error;
  ad = this.facade.vm;
  mainImage = this.facade.mainImage;

  myOffers = this.facade.myOffers;
  myOffersLoading = this.facade.myOffersLoading;

  blobUrl?: string;

  toSrc = (raw?: string | null): string | null => {
    if (!raw) return null;

    if (isHttp(raw) || isData(raw)) return raw;

    if (raw.startsWith('/')) {
      return `${environment.apiBaseUrl}${raw}`;
    }

    if (raw.startsWith('?') || raw.length < 20) return null;

    const mime = looksJpeg(raw)
      ? 'image/jpeg'
      : looksPng(raw)
        ? 'image/png'
        : 'image/*';
    return `data:${mime};base64,${raw}`;
  };

  constructor() {
    const idParam =
      this.route.snapshot.paramMap.get('detailId') ??
      this.route.snapshot.paramMap.get('id');

    const detailId = idParam != null ? Number(idParam) : NaN;

    const userEmail = this.authService.getEmail();

    this.facade.loadByRealEstateId(detailId, { userEmail });
  }

  setMain(src: string) {
    this.facade.setMain(src);
  }

  isLogged() {
    return !!this.authService.isAuthenticated?.();
  }

  goLogin() {
    this.router.navigateByUrl('/auth/login');
  }

  onOfferSuccess() {
    const current = this.ad();
    if (current?.realEstateId != null) {
      this.facade.loadMyOffers(current.realEstateId);
    }
  }

  onVisitSuccess() {}

  loadImageBlob(url: string) {
    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (blob) => {
        if (this.blobUrl) URL.revokeObjectURL(this.blobUrl);
        this.blobUrl = URL.createObjectURL(blob);
      },
      error: () => {
        if (this.blobUrl) URL.revokeObjectURL(this.blobUrl);
        this.blobUrl = undefined;
      },
    });
  }

  badgeClass(status: string) {
    switch (status) {
      case 'ACCEPTED':
        return 'accepted_offer_badge';
      case 'REJECTED':
        return 'rejected_offer_badge';
      case 'COUNTERED':
        return 'countered_offer_badge';
      case 'COUNTER_OFFER':
        return 'counter_offer_badge';
      default:
        return 'pending_offer_badge';
    }
  }

  mapTag(t?: string): string {
    switch (t) {
      case 'NEAR_SCHOOLS':
        return 'Vicino a scuole';
      case 'NEAR_PARKS':
        return 'Vicino a parchi';
      case 'NEAR_PUBLIC_TRANSPORT':
        return 'Vicina a trasporto pubblico';
      default:
        return t ?? '';
    }
  }
}
