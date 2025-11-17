import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { isEstateAgentGuard } from './is-estate-agent.guard';

describe('isEstateAgentGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => isEstateAgentGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
