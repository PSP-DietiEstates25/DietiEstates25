import { Component, inject, computed } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AdDetailFacade } from './ad-detail.facade';
import { OfferFormComponent } from '../offer/offer-form.component';
import { VisitFormComponent } from '../visit/visit-form.component';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-ad-detail',
  standalone: true,
  imports: [RouterLink, OfferFormComponent, VisitFormComponent, DecimalPipe],
  templateUrl: './ad-detail.component.html',
})
export class AdDetailComponent {
  private route = inject(ActivatedRoute);
  private facade = inject(AdDetailFacade);

  // re-export signals al template
  loading = this.facade.loading;
  error = this.facade.error;
  ad = this.facade.vm;
  mainImage = this.facade.mainImage;

  constructor() {
    const param =
      this.route.snapshot.paramMap.get('detailId') ??
      this.route.snapshot.paramMap.get('id');
    const detailId = Number(param);
    // se hai userEmail/category dal contesto auth/rotta, passali qui:
    this.facade.loadByDetailId(detailId, {
      userEmail: 'guest@public.local',
      // category: 'SALE'
    });
  }

  setMain(src: string) {
    this.facade.setMain(src);
  }

  isLogged() {
    // collegamento ad auth state vero
    if (localStorage.getItem('user')) {
      return true;
    }

    return false;
  }
}
