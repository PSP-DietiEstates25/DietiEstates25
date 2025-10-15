import { TestBed } from '@angular/core/testing';

import { AuthManualService } from './auth-manual.service';

describe('AuthManualService', () => {
  let service: AuthManualService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthManualService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
