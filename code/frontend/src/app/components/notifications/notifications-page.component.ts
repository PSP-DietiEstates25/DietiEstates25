import { Component, HostListener, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificationsFacade } from './notifications.facade';
import { NotificationCategory } from './notification-preferences.adapter';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";

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
    prefs: this.facade.userPreferences,
    filtered: this.facade.filtered,
  };

  readonly categories: NotificationCategory[] = [
    'NEW_PROPERTIES',
    'PROMOTIONAL',
    'VISIT',
    'OFFER',
  ];

  constructor() {
    this.facade.init();
  }

  onToggle(cat: string, ev: Event) {
    const enabled = (ev.target as HTMLInputElement).checked;
    this.facade.setCategoryEnabled(cat as NotificationCategory, enabled);
  }
  onFilterCat(cat: NotificationCategory) {
    this.facade.toggleFilterCat(cat);
  }
  onQuery(q: string) {
    this.facade.setQuery(q ?? '');
  }
  onClearFilters() {
    this.facade.clearFilters();
  }

  @HostListener('window:scroll', [])
  onScroll() {
    if (this.vm.loading()) return;
    const nearBottom =
      window.innerHeight + window.scrollY >= document.body.offsetHeight - 300;
    if (nearBottom) this.facade.loadMore();
  }
}
