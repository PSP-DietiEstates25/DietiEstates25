import { TestBed } from '@angular/core/testing';

import { SearchPaginatorService } from './search-paginator.service';

describe('SearchPaginatorService', () => {
  let service: SearchPaginatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchPaginatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
