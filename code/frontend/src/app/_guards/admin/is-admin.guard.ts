import { CanActivateFn } from '@angular/router';
import { AuthService } from '../../manual_services/auth.service';
import { inject } from '@angular/core';

export const isAdminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  return authService.isAdmin();
};
