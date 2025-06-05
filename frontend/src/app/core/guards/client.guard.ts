import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map, take } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class ClientGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate() {
    return this.auth.userRole$.pipe(
      take(1),
      map(role => {
        if (role === 'CLIENT') {
          return true;
        } else {
          this.router.navigate(['/agent']); // o /admin, oppure /
          return false;
        }
      })
    );
  }
}