import { CanActivateFn, CanMatchFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService, Role } from './auth.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (auth.authState().isAuthenticated) return true;
  router.navigate(['/auth']);
  return false;
};

export const roleGuard: CanMatchFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const required = route.data?.['requiredRole'] as Role | undefined;
  if (!required) return true;
  if (auth.authState().isAuthenticated && auth.role() === required) return true;
  router.navigate(['/']);
  return false;
};
