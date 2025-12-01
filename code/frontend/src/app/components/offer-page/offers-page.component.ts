import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { OffersFacade } from './offers.facade';

@Component({
  selector: 'app-offers-page',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './offers-page.component.html',
})
export class OffersPageComponent implements OnInit {
  readonly facade = inject(OffersFacade);

  ngOnInit(): void {
    this.facade.init();
  }

  badgeClass(status: string) {
    switch (status) {
      case 'ACCEPTED': return 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/30 dark:text-emerald-200';
      case 'REJECTED': return 'bg-rose-100 text-rose-800 dark:bg-rose-900/30 dark:text-rose-200';
      case 'COUNTERED': return 'bg-amber-100 text-amber-800 dark:bg-amber-900/30 dark:text-amber-200';
      default: return 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-200';
    }
  }
}
