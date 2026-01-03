import { Component, inject, OnDestroy } from '@angular/core';
import { AgentDashboardFacade } from '../agent-dashboard/agent-dashboard.facade';
import { FullOffer } from '../../interfaces/full-offer';
import { OfferControllerService } from '../../services/services';
import { OffersPaginatorService } from '../../manual_services/offers_paginator/offers-paginator.service';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-agent-offers-list',
  imports: [FormsModule, DatePipe],
  templateUrl: './agent-offers-list.component.html',
  styleUrl: './agent-offers-list.component.scss',
})
export class AgentOffersListComponent implements OnDestroy {
  facade = inject(AgentDashboardFacade);
  offerService = inject(OfferControllerService);
  offerPaginatorService = inject(OffersPaginatorService);
  toastrService = inject(ToastrService);

  counterAmount = this.facade.counterAmount;
  counterId = this.facade.counterId;
  offers = this.facade.offers;

  acceptOffer(offer: FullOffer) {
    this.facade.acceptOffer(offer).subscribe({
      next: () => this.toastrService.success('Offerta accettata con successo'),
      error: () => this.toastrService.error("Errore durante l'operazione"),
    });
  }

  declineOffer(offer: FullOffer) {
    this.facade.declineOffer(offer).subscribe({
      next: () => this.toastrService.success('Offerta rifiutata'),
      error: () => this.toastrService.error("Errore durante l'operazione"),
    });
  }

  startAddOfferFor(realEstateId: number) {
    this.facade.startAddOfferFor(realEstateId);
  }

  cancelAddOffer() {
    this.facade.cancelAddOffer();
  }

  submitExternalOffer() {
    this.facade.createExternalOffer().subscribe();
  }

  // Counter-offer
  startCounter(offer: FullOffer) {
    this.facade.startCounter(offer);
  }

  cancelCounter() {
    this.facade.cancelCounter();
  }

  sendCounter() {
    this.facade.sendCounter().subscribe({
      next: () => {
        this.toastrService.success('Controfferta inviata con successo');
      },
      error: () => {
        this.toastrService.error("Errore durante l'invio della controfferta");
      },
    });
  }

  ngOnDestroy(): void {
    this.offerPaginatorService.refresh();
  }

  badgeClass(status: string) {
    switch (status) {
      case 'ACCEPTED':
        return 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/30 dark:text-emerald-200';
      case 'REJECTED':
        return 'bg-rose-100 text-rose-800 dark:bg-rose-900/30 dark:text-rose-200';
      case 'COUNTERED':
        return 'bg-amber-100 text-amber-800 dark:bg-amber-900/30 dark:text-amber-200';
      case 'COUNTER_OFFER':
        return 'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-200';
      default:
        return 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-200';
    }
  }

  deletedRealEstateBadge(){
    return 'real_estate_deleted_badge';
  }
}
