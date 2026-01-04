import {
  Component,
  computed,
  effect,
  inject,
  OnDestroy,
  Signal,
  signal,
  WritableSignal,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { AgentDashboardFacade } from '../agent-dashboard/agent-dashboard.facade';
import { AdsPaginatorService } from '../../manual_services/ads_paginator/ads-paginator.service';
import { environment } from '../../../environments/environment';
import { NearParkIconComponent } from '../../shared/icons/near-park-icon/near-park-icon.component';
import { NearPublicTransportIconComponent } from '../../shared/icons/near-public-transport-icon/near-public-transport-icon.component';
import { NearSchoolIconComponent } from '../../shared/icons/near-school-icon/near-school-icon.component';
import { PriceIconComponent } from '../../shared/icons/price-icon/price-icon.component';
import { SquareMetersIconComponent } from '../../shared/icons/square-meters-icon/square-meters-icon.component';
import { FloorIconComponent } from '../../shared/icons/floor-icon/floor-icon.component';
import { RoomsIconComponent } from '../../shared/icons/rooms-icon/rooms-icon.component';
import { EnergyClassIconComponent } from '../../shared/icons/energy-class-icon/energy-class-icon.component';
import { AirConditioningIconComponent } from '../../shared/icons/air-conditioning-icon/air-conditioning-icon.component';
import { ElevatorIconComponent } from '../../shared/icons/elevator-icon/elevator-icon.component';
import { DoormanIconComponent } from '../../shared/icons/doorman-icon/doorman-icon.component';
import { ToastrService } from 'ngx-toastr';
import { DeleteAdDialogComponent } from '../dialog/delete-ad-dialog/delete-ad-dialog.component';
import { HttpErrorResponse } from '@angular/common/http';
import { FullRealEstate } from '../../interfaces/full-real-estate';
import { AdsPaginatorComponent } from '../ads-paginator/ads-paginator.component';

@Component({
  selector: 'app-agent-ads-list',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    DatePipe,
    NearParkIconComponent,
    NearPublicTransportIconComponent,
    NearSchoolIconComponent,
    PriceIconComponent,
    SquareMetersIconComponent,
    FloorIconComponent,
    RoomsIconComponent,
    EnergyClassIconComponent,
    AirConditioningIconComponent,
    ElevatorIconComponent,
    DoormanIconComponent,
    DeleteAdDialogComponent,
    AdsPaginatorComponent
  ],
  templateUrl: './agent-ads-list.component.html',
  styleUrl: './agent-ads-list.component.scss',
})
export class AgentAdsListComponent implements OnDestroy {
  facade = inject(AgentDashboardFacade);
  adsPaginatorService = inject(AdsPaginatorService);
  toastrService = inject(ToastrService);

  isDiscardModalOpen = false;

  realEstates!: Signal<FullRealEstate[]>;
  realEstateIdToRemove: WritableSignal<number | null> = signal(null);
  realEstatesLoading = this.facade.realEstatesLoading;

  //offerta esterna
  addOfferForId = this.facade.addOfferForId;
  addOfferAmount = this.facade.addOfferAmount;
  addOfferEmail = this.facade.addOfferEmail;
  addOfferLoading = this.facade.addOfferLoading;

  constructor() {
    effect(() => {
      this.realEstates = computed(() => this.facade.realEstates());
    });
  }

  badgeClass(){
    return 'real_estate_category_badge';
  }
  
  getImageUrl(path?: string) {
    return `${environment.apiBaseUrl}${path}`;
  }

  openDiscardModal(adId: number) {
    this.realEstateIdToRemove.set(adId);
    this.isDiscardModalOpen = true;
  }

  closeDiscardModal() {
    this.realEstateIdToRemove.set(null);
    this.isDiscardModalOpen = false;
  }

  confirmDiscard() {
    const realEstateToRemoveId = this.realEstateIdToRemove();
    if (realEstateToRemoveId != null) {
      this.deleteAd(realEstateToRemoveId);
    }
    this.closeDiscardModal();
  }

  deleteAd(adId: number) {
    this.facade.deleteAd(adId).subscribe({
      next: () => {
        this.toastrService.success('Annuncio cancellato');
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 500) {
          this.toastrService.error(
            'Errore interno del server',
            'Contatta un admin',
          );
        }
      },
    });
  }

  startAddOfferFor(adId: number) {
    this.facade.startAddOfferFor(adId);
  }

  submitExternalOffer() {
    this.facade.createExternalOffer().subscribe();
  }

  cancelAddOffer() {
    this.facade.cancelAddOffer();
  }

  ngOnDestroy(): void {
    this.adsPaginatorService.refresh();
  }
}
