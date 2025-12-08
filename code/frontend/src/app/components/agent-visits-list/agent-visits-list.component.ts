import { Component, inject, OnDestroy } from '@angular/core';
import { AgentDashboardComponent } from '../agent-dashboard/agent-dashboard.component';
import { AgentDashboardFacade } from '../agent-dashboard/agent-dashboard.facade';
import { VisitControllerService } from '../../services/services';
import { VisitPaginatorService } from '../../manual_services/visit_paginator/visit-paginator.service';
import { ToastrService } from 'ngx-toastr';
import { VisitResponse } from '../../services/models';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-agent-visits-list',
  imports: [DatePipe, RouterLink],
  templateUrl: './agent-visits-list.component.html',
  styleUrl: './agent-visits-list.component.scss',
})
export class AgentVisitsListComponent implements OnDestroy {
  facade = inject(AgentDashboardFacade);
  visitService = inject(VisitControllerService);
  visitPaginatorService = inject(VisitPaginatorService);
  toastrService = inject(ToastrService);

  visits = this.facade.visits;

  approveVisit(visit: VisitResponse) {
    this.facade.approveVisit(visit).subscribe({
      next: () => this.toastrService.success('Visita accettata con successo'),
      error: () => this.toastrService.error("Errore durante l'operazione"),
    });
  }

  declineVisit(visit: VisitResponse) {
    this.facade.declineVisit(visit).subscribe({
      next: () => this.toastrService.success('Visita rifiutata'),
      error: () => this.toastrService.error("Errore durante l'operazione"),
    });
  }

  ngOnDestroy(): void {
    this.visitPaginatorService.refresh();
  }

  badgeClass(status: string) {
    switch (status) {
      case 'ACCEPTED':
        return 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/30 dark:text-emerald-200';
      case 'REJECTED':
        return 'bg-rose-100 text-rose-800 dark:bg-rose-900/30 dark:text-rose-200';
      default:
        return 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-200';
    }
  }
}
