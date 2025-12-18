import { TestBed } from '@angular/core/testing';

import { NotificationPaginatorService } from './notification-paginator.service';

describe('NotificationPaginatorService', () => {
  let service: NotificationPaginatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationPaginatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
