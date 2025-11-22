import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AdDetailFacade } from './ad-detail.facade';
import { OfferFormComponent } from '../offer/offer-form.component';
import { VisitFormComponent } from '../visit/visit-form.component';
import { DecimalPipe, DatePipe } from '@angular/common';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../manual_services/auth.service';

import { environment } from '../../../environments/environment';

const isHttp = (s: string) => /^https?:\/\//i.test(s);
const isData = (s: string) => /^data:/i.test(s);
const looksJpeg = (b64: string) => b64.startsWith('/9j/');
const looksPng = (b64: string) => b64.startsWith('iVBOR');

@Component({
  selector: 'app-ad-detail',
  standalone: true,
  imports: [
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
