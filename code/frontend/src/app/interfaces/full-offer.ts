import { Offer, OfferResponse } from '../services/models';

export interface FullOffer extends OfferResponse {
  counterOffer: Offer | null;
}
