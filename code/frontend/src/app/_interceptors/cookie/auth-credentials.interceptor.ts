import { HttpErrorResponse, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { Observable, EMPTY, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

/**
 * Adds withCredentials ONLY for calls that are meant to hit the BFF.
 *
 * Works with:
 *  - absolute baseUrl (dev):  http://localhost:8080
 *  - relative baseUrl (prod): /api
 */
function isAbsoluteUrl(url: string): boolean {
  return /^https?:\/\//i.test(url);
}

function normalizeBase(base: string): string {
  return (base ?? '').trim().replace(/\/+$/, '');
}

const API_BASE = normalizeBase(environment.apiBaseUrl);

export const authCredentials: HttpInterceptorFn = (req, next): Observable<HttpEvent<unknown>> => {
  const reqIsAbs = isAbsoluteUrl(req.url);

  const bffOrigin = isAbsoluteUrl(API_BASE) ? new URL(API_BASE).origin : (typeof window !== 'undefined' ? window.location.origin : '');

  const hitsBff = (() => {
    if (reqIsAbs) {
      return !!bffOrigin && req.url.startsWith(bffOrigin);
    }

    if (!API_BASE || API_BASE === '/') {
      return req.url.startsWith('/');
    }

    if (!isAbsoluteUrl(API_BASE) && API_BASE.startsWith('/')) {
      return req.url === API_BASE || req.url.startsWith(API_BASE + '/');
    }

    return typeof window !== 'undefined' && window.location.origin === bffOrigin && req.url.startsWith('/');
  })();

  const reqWithCreds =
    hitsBff && !req.withCredentials ? req.clone({ withCredentials: true }) : req;

  return next(reqWithCreds).pipe(
    catchError((err: unknown) => {
      if (err instanceof HttpErrorResponse && err.status === 401 && hitsBff) {
        window.location.assign(environment.loginUrl);
        return EMPTY;
      }
      return throwError(() => err);
    })
  );
};
