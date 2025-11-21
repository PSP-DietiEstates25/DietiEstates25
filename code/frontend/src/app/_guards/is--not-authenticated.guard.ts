import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../manual_services/auth.service';

export const isNotAuthenticatedGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const role = authService.getRole();
  return role == null;
};
