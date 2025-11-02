// auth-interceptor.fn.ts
import { HttpErrorResponse, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { Observable, EMPTY, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

const BFF_ORIGINS = ['http://localhost:8080']; // aggiungi qui eventuali altri host del BFF

export const authCredentials: HttpInterceptorFn = (req, next): Observable<HttpEvent<unknown>> => {
  // decide se aggiungere withCredentials
  const isAbsolute = /^https?:\/\//i.test(req.url);
  const hitsBff =
    (!isAbsolute && req.url.startsWith('/')) || // chiamate relative (es. '/realestates')
    BFF_ORIGINS.some(origin => req.url.startsWith(origin)); // chiamate assolute al BFF

  const reqWithCreds = (hitsBff && !req.withCredentials) ? req.clone({ withCredentials: true }) : req;

  return next(reqWithCreds).pipe(
    catchError((err: unknown) => {
      if (err instanceof HttpErrorResponse && err.status === 401 && hitsBff) {
        // avvia login OIDC sul BFF (top-level redirect)
        window.location.href = 'http://localhost:8080/oauth2/authorization/messaging-client-oidc';
        return EMPTY; // interrompe la catena
      }
      return throwError(() => err);
    })
  );
};

