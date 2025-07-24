import { Component, inject } from '@angular/core';
import { Offer } from '../../interfaces/offer.interface';
import { OffersService } from '../../services/offers.service';
import { MatListItem, MatList, MatActionList } from "@angular/material/list";

@Component({
  selector: 'app-offer-list',
  imports: [MatList, MatListItem, MatActionList],
  templateUrl: './offer-list.component.html',
  styleUrl: './offer-list.component.scss',
  providers: [OffersService]
})
export class OfferListComponent {

  offers: Offer[] = []
  selectedOffer: Offer | undefined;
  private readonly offersService: OffersService = inject(OffersService);


}
