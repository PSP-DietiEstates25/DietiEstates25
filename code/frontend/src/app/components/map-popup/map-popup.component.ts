import { Component, inject, input, Input } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { CurrencyPipe } from '@angular/common';
import { FullRealEstate } from '../../interfaces/full-real-estate';
import { RoomsIconComponent } from '../../shared/icons/rooms-icon/rooms-icon.component';
import { SquareMetersIconComponent } from '../../shared/icons/square-meters-icon/square-meters-icon.component';

@Component({
  selector: 'app-map-popup',
  standalone: true,
  imports: [CurrencyPipe, RoomsIconComponent, SquareMetersIconComponent],
  templateUrl: './map-popup.component.html',
  styleUrl: './map-popup.component.scss',
})
export class MapPopupComponent {
  @Input() cards: FullRealEstate[] = [];
  currentIndex = 0;

  private routerService = inject(Router);

  get currentCard() {
    return this.cards![this.currentIndex];
  }

  getImageUrl(path?: string): string {
    return `${environment.apiBaseUrl}${path}`;
  }

  getCategoryLabel(category?: string): string {
    return (category || 'SALE') === 'RENT' ? 'Affitto' : 'Vendita';
  }

  getAddress(card: any): string {
    return [card.geographicalPosition.address, card.geographicalPosition.city]
      .filter(Boolean)
      .join(', ');
  }

  next(event: Event) {
    event.stopPropagation();
    event.preventDefault();
    this.currentIndex = (this.currentIndex + 1) % this.cards.length;
  }

  prev(event: Event) {
    event.stopPropagation();
    event.preventDefault();
    this.currentIndex =
      (this.currentIndex - 1 + this.cards.length) % this.cards.length;
  }

  goToDetail(event: Event) {
    event.stopPropagation();
    event.preventDefault();
    if (this.currentCard?.id) {
      this.routerService.navigate(['/ad', this.currentCard.id]);
    }
  }
}
