import { Component, inject, OnDestroy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { AgentDashboardFacade } from '../agent-dashboard/agent-dashboard.facade'; // Verifica il percorso corretto
import { AdsPaginatorService } from '../../manual_services/ads_paginator/ads-paginator.service';
import { environment } from '../../../environments/environment';
import { NearParkIconComponent } from '../../shared/icons/near-park-icon/near-park-icon.component';
import { NearPublicTransportIconComponent } from '../../shared/icons/near-public-transport-icon/near-public-transport-icon.component';
import { NearSchoolIconComponent } from '../../shared/icons/near-school-icon/near-school-icon.component';
import { PriceIconComponent } from '../../shared/icons/price-icon/price-icon.component';
import { SquareMetersIconComponent } from '../../shared/icons/square-meters-icon/square-meters-icon.component';
import { FloorIconComponent } from '../../shared/icons/floor-icon/floor-icon.component';
import { RoomsIconComponent } from '../../shared/icons/rooms-icon/rooms-icon.component';
import { EnergyClass } from '../../enums/energy-class.enum';
import { GeographicalPositionIconComponent } from '../../shared/icons/geographical-position-icon/geographical-position-icon.component';
import { EnergyClassIconComponent } from '../../shared/icons/energy-class-icon/energy-class-icon.component';
import { AirConditioningIconComponent } from '../../shared/icons/air-conditioning-icon/air-conditioning-icon.component';
import { ElevatorIconComponent } from '../../shared/icons/elevator-icon/elevator-icon.component';
import { DoormanIconComponent } from '../../shared/icons/doorman-icon/doorman-icon.component';

@Component({
  selector: 'app-agent-ads-list',
  standalone: true, // Assicurati che sia standalone
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
    GeographicalPositionIconComponent,
    AirConditioningIconComponent,
    ElevatorIconComponent,
    DoormanIconComponent,
  ],
  templateUrl: './agent-ads-list.component.html',
  styleUrl: './agent-ads-list.component.scss',
})
export class AgentAdsListComponent implements OnDestroy {
  private facade = inject(AgentDashboardFacade);
  private adsPaginatorService = inject(AdsPaginatorService);

  realEstates = this.facade.realEstates;
  realEstatesLoading = this.facade.realEstatesLoading;

  //offerta esterna
  addOfferForId = this.facade.addOfferForId;
  addOfferAmount = this.facade.addOfferAmount;
  addOfferEmail = this.facade.addOfferEmail;
  addOfferLoading = this.facade.addOfferLoading;

  getImageUrl(path?: string) {
    return `${environment.apiBaseUrl}${path}`;
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

  deleteAd(adId: number) {
    if (confirm('Sei sicuro di voler eliminare questo annuncio?')) {
      this.facade.deleteAd(adId).subscribe();
    }
  }

  ngOnDestroy(): void {
    this.adsPaginatorService.refresh();
  }
}
