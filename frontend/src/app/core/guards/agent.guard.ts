import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map, take } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AgentGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate() {
    return this.auth.userRole$.pipe(
      take(1),
      map(role => {
        if (role === 'AGENT') {
          return true;
        } else {
          // reindirizzo da agente a home
          this.router.navigate(['/']);
          return false;
        }
      })
    );
  }
}