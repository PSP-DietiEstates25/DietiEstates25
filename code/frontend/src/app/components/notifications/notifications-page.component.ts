import { Component, HostListener, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificationsFacade } from './notifications.facade';
import { NotificationCategory } from './notification-preferences.adapter';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';

@Component({
  selector: 'app-notifications-page',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './notifications-page.component.html',
})
export class NotificationsPageComponent {
  readonly facade = inject(NotificationsFacade);

  readonly vm = {
    loading: this.facade.loading,
    error: this.facade.error,
    prefs: this.facade.userPreferences,
    filtered: this.facade.filtered,
    filterCats: this.facade.filterCategories,
  };

  readonly categories: NotificationCategory[] = [
    'NEW_PROPERTIES',
    'PROMOTIONAL',
    'VISIT',
    'OFFER',
  ];

  constructor() {
    this.facade.init();
    this.facade.markAllSeen();
  }

  onToggle(category: string, ev: Event) {
    const enabled = (ev.target as HTMLInputElement).checked;
    this.facade.setCategoryEnabled(category as NotificationCategory, enabled);
  }

  onFilterCat(category: NotificationCategory) {
    this.facade.toggleFilterCat(category);
  }

  onQuery(q: string) {
    this.facade.setQuery(q ?? '');
  }

  onClearFilters() {
    this.facade.clearFilters();
  }

  labelOf(cat: NotificationCategory): string {
    switch (cat) {
      case 'NEW_PROPERTIES':
        return 'Nuovi immobili';
      case 'PROMOTIONAL':
        return 'Promozioni';
      case 'VISIT':
        return 'Visite';
      case 'OFFER':
        return 'Offerte';
    }
  }

  hintOf(cat: NotificationCategory): string {
    switch (cat) {
      case 'OFFER':
        return 'Controlla i dettagli dell’offerta e lo stato della trattativa.';
      case 'VISIT':
        return 'Verifica data/ora.';
      case 'NEW_PROPERTIES':
        return 'Nuovi annunci compatibili con le tue preferenze.';
      case 'PROMOTIONAL':
        return 'Comunicazioni e aggiornamenti informativi.';
    }
  }

  relativeTime(iso: string): string {
    const t = new Date(iso).getTime();
    const diff = Date.now() - t;
    const m = Math.floor(diff / 60000);
    if (m < 1) return 'adesso';
    if (m < 60) return `${m} min fa`;
    const h = Math.floor(m / 60);
    if (h < 24) return `${h} h fa`;
    const d = Math.floor(h / 24);
    return `${d} g fa`;
  }

  getOfferAcceptedMessage(offerAmount: number, estateAgentEmail: string) {
    return `L'offerta di ${offerAmount} è stata accettata, contatta l'agente al seguente recapito: ${estateAgentEmail}.`;
  }

  getOfferRejectedMessage(offerAmount: number) {
    return `L'offerta di ${offerAmount} è stata rifiutata, riprova con una nuova offerta.`;
  }

  getVisitAcceptedMessage(visitDate: string, visitTime: string) {
    return `La visita prenotata per il giorno ${visitDate} alle ${visitTime} è stata accettata.`;
  }

  getVisitRejectedMessage(visitDate: string, visitTime: string) {
    return `La visita prenotata per il giorno ${visitDate} alle ${visitTime} è stata rifiutata.`;
  }

  getNewPropertyMessage() {
    return `È presente un nuovo immobile`;
  }
  getPromotionalMessage() {
    return 'Messaggio promozionale';
  }
  /*
  openFromNotification(n: { category: NotificationCategory; id: number }) {
  // placeholder: senza targetId dal backend puoi solo portare a una pagina generica
  // es: navigate a /offers, /visits, ecc.
  // this.router.navigate(...)
    console.log('open', n);
  }
  */

  @HostListener('window:scroll', [])
  onScroll() {
    if (this.vm.loading()) return;
    const nearBottom =
      window.innerHeight + window.scrollY >= document.body.offsetHeight - 300;
    if (nearBottom) {
      this.facade.loadMore();
    }
  }
}
