import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../vecchioService/auth/auth.service';
import { take, map } from 'rxjs/operators';

export function roleGuard(
  allowed: Array<'CLIENT' | 'AGENT' | 'ADMIN'>,
  redirectIfDenied = '/'
): CanActivateFn {
  return () => {
    const auth = inject(AuthService);
    const router = inject(Router);

    return auth.userRole$.pipe(
      take(1),
      map((role: 'CLIENT' | 'AGENT' | 'ADMIN' | null) => {
        const ok = role !== null && allowed.includes(role);
        if (ok) return true;
        router.navigate([redirectIfDenied]);
        return false;
      })
    );
  };
}
