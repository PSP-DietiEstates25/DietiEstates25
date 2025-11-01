import { inject, Injectable, signal } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, finalize, map, tap } from 'rxjs/operators';
import { AdminControllerService } from '../../services/services';
import { AuthAccountService } from '../../services_server/auth-account.service';

@Injectable({ providedIn: 'root' })
export class AdminAccountFacade {
  loading = signal(false);
  ok = signal<string | null>(null);
  err = signal<string | null>(null);

  private api = inject(AuthAccountService);

  changePassword(
    currentPassword: string,
    newPassword: string
  ): Observable<void> {
    this.loading.set(true);
    this.ok.set(null);
    this.err.set(null);

    return this.api
      .changePassword({ oldPassword: currentPassword, newPassword })
      .pipe(
        tap(() =>
          this.ok.set('Password aggiornata. Effettua di nuovo l’accesso.')
        ),
        map(() => void 0),
        catchError((e) => {
          if (e?.status === 400) {
            this.err.set('La password corrente non è corretta.');
          } else if (e?.status === 401) {
            this.err.set('Sessione scaduta: accedi di nuovo.');
          } else if (e?.status === 403) {
            this.err.set('Non hai i permessi per questa operazione.');
          } else {
            this.err.set('Errore durante l’aggiornamento della password.');
          }
          return of(void 0);
        }),
        finalize(() => this.loading.set(false))
      );
  }
}
