import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../../manual_services/auth.service';

export const isEstateAgentOrAdminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  return authService.isEstateAgent() || authService.isAdmin();
};
