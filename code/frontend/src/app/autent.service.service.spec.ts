import { TestBed } from '@angular/core/testing';

import { AutentServiceService } from './autent.service.service';

describe('AutentServiceService', () => {
  let service: AutentServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AutentServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
