import { TestBed } from '@angular/core/testing';

import { AdsPaginatorService } from './ads-paginator.service';

describe('AdsPaginatorService', () => {
  let service: AdsPaginatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdsPaginatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
