import { TestBed } from '@angular/core/testing';

import { OffersPaginatorService } from '../../offers-paginator.service';

describe('OffersPaginatorService', () => {
  let service: OffersPaginatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OffersPaginatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
