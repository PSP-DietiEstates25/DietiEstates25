import { inject, Injectable, signal } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, finalize, map, tap } from 'rxjs/operators';
import { AdminControllerService } from '../../services/services';
import { AuthAccountService } from '../../services_server/auth-account.service';

@Injectable({ providedIn: 'root' })
export class AdminAccountFacade {
  loading = signal(false);
  success = signal<string | null>(null);
  error = signal<string | null>(null);

  private authAccountService = inject(AuthAccountService);

  changePassword(
    currentPassword: string,
    newPassword: string
  ): Observable<void> {
    this.loading.set(true);
    this.success.set(null);
    this.error.set(null);

    return this.authAccountService
      .changePassword({ oldPassword: currentPassword, newPassword })
      .pipe(
        tap(() =>
          this.success.set('Password aggiornata. Effettua di nuovo l’accesso.')
        ),
        map(() => void 0),
        catchError((error) => {
          if (error?.status === 400) {
            this.error.set('La password corrente non è corretta.');
          } else if (error?.status === 401) {
            this.error.set('Sessione scaduta: accedi di nuovo.');
          } else if (error?.status === 403) {
            this.error.set('Non hai i permessi per questa operazione.');
          } else {
            this.error.set('Errore durante l’aggiornamento della password.');
          }
          return of(void 0);
        }),
        finalize(() => this.loading.set(false))
      );
  }
}
