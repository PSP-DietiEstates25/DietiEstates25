import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import {
  AdService,
  AdDetail,
} from '../../vecchioService/rest-backend/ad/ad.service';
import { AuthService } from '../../vecchioService/auth/auth.service';

import { OfferFormComponent } from '../offer/offer-form.component';
import { VisitFormComponent } from '../visit/visit-form.component';

@Component({
  selector: 'app-ad-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, OfferFormComponent, VisitFormComponent],
  templateUrl: './ad-detail.component.html',
})
export class AdDetailComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private api = inject(AdService);
  private auth = inject(AuthService);

  ad = signal<AdDetail | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);
  mainImage = signal<string | null>(null);

  isLogged = computed(() => this.auth.authState().isAuthenticated);

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isFinite(id)) {
      this.error.set('Annuncio non trovato');
      this.loading.set(false);
      return;
    }
    this.api.getById(id).subscribe({
      next: (data) => {
        this.ad.set(data);
        this.mainImage.set(data?.images?.[0] || data?.coverUrl || null);
      },
      error: () => this.error.set("Impossibile caricare l'annuncio"),
      complete: () => this.loading.set(false),
    });
  }

  setMain(src: string) {
    this.mainImage.set(src);
  }

  goToLogin() {
    this.router.navigate(['/auth/login'], {
      queryParams: { redirect: this.router.url },
    });
  }
}
