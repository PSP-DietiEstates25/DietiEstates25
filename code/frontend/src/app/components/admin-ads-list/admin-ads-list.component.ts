import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  inject,
  OnDestroy,
  signal,
  WritableSignal,
} from '@angular/core';
import { environment } from '../../../environments/environment';
import { AdsPaginatorService } from '../../manual_services/ads_paginator/ads-paginator.service';
import { ToastrService } from 'ngx-toastr';
import { DeleteAdDialogComponent } from '../dialog/delete-ad-dialog/delete-ad-dialog.component';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { PriceIconComponent } from '../../shared/icons/price-icon/price-icon.component';
import { RoomsIconComponent } from '../../shared/icons/rooms-icon/rooms-icon.component';
import { SquareMetersIconComponent } from '../../shared/icons/square-meters-icon/square-meters-icon.component';
import { FloorIconComponent } from '../../shared/icons/floor-icon/floor-icon.component';
import { EnergyClassIconComponent } from '../../shared/icons/energy-class-icon/energy-class-icon.component';
import { AirConditioningIconComponent } from '../../shared/icons/air-conditioning-icon/air-conditioning-icon.component';
import { ElevatorIconComponent } from '../../shared/icons/elevator-icon/elevator-icon.component';
import { DoormanIconComponent } from '../../shared/icons/doorman-icon/doorman-icon.component';
import { NearParkIconComponent } from '../../shared/icons/near-park-icon/near-park-icon.component';
import { NearSchoolIconComponent } from '../../shared/icons/near-school-icon/near-school-icon.component';
import { NearPublicTransportIconComponent } from '../../shared/icons/near-public-transport-icon/near-public-transport-icon.component';
import { AdminDashboardFacade } from '../admin-dashboard/admin-dashboard.facade';

@Component({
  selector: 'app-admin-ads-list',
  imports: [
    DeleteAdDialogComponent,
    RouterLink,
    DatePipe,
    PriceIconComponent,
    RoomsIconComponent,
    SquareMetersIconComponent,
    FloorIconComponent,
    EnergyClassIconComponent,
    AirConditioningIconComponent,
    ElevatorIconComponent,
    DoormanIconComponent,
    NearParkIconComponent,
    NearSchoolIconComponent,
    NearPublicTransportIconComponent,
  ],
  templateUrl: './admin-ads-list.component.html',
  styleUrl: './admin-ads-list.component.scss',
})
export class AdminAdsListComponent implements OnDestroy {
  facade = inject(AdminDashboardFacade);
  adsPaginatorService = inject(AdsPaginatorService);
  toastrService = inject(ToastrService);

  isDiscardModalOpen = false;

  realEstates = this.facade.realEstates;
  realEstateIdToRemove: WritableSignal<number | null> = signal(null);
  realEstatesLoading = this.facade.realEstatesLoading;

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

  ngOnDestroy(): void {
    this.adsPaginatorService.refresh();
  }
}
