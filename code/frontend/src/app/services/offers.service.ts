import { Injectable } from '@angular/core';
import { Offer } from '../interfaces/offer.interface';
import { Status } from '../enums/status.enum';

@Injectable()
export class OffersService {
  
  getOffers(): Offer[] {
    return [
      {
        proposalId: 0,
        status: Status.Pending,
        amount: 50
      },
      {
        proposalId: 1,
        status: Status.Accepted,
        amount: 100
      },
      {
        proposalId: 2,
        status: Status.Rejected,
        amount: 150
      },
    ];
  }
}
