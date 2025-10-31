import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthStore, Role } from './auth.store';

export function roleGuard(
  allowed: Role[],
  redirectIfDenied: string = '/auth'
): CanActivateFn {
  return () => {
    const router = inject(Router);
    const auth = inject(AuthStore);

    const role = auth.role();
    const ok = !!role && allowed.includes(role);

    if (ok) return true;

    if (!auth.isAuthenticated()) 
      return router.createUrlTree(['/auth']);
    
    router.navigate([redirectIfDenied]);
    return false;
  };
}
