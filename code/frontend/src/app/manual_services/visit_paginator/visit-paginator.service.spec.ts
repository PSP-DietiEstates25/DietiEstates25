import { TestBed } from '@angular/core/testing';

import { VisitPaginatorService } from './visit-paginator.service';

describe('VisitPaginatorService', () => {
  let service: VisitPaginatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VisitPaginatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
