import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { environment } from '../../../environments/environment';

const BFF_ORIGIN = new URL(environment.apiBaseUrl).origin;

export const xsrfInterceptor: HttpInterceptorFn = (req, next) => {
  const isAbsolute = /^https?:\/\//i.test(req.url);
  const hitsBff =
    (isAbsolute && req.url.startsWith(BFF_ORIGIN)) ||
    (!isAbsolute && typeof window !== 'undefined' && window.location.origin === BFF_ORIGIN && req.url.startsWith('/'));

  if (hitsBff && ['POST','PUT','PATCH','DELETE'].includes(req.method)) {
    const token = inject(CookieService).get('XSRF-TOKEN');
    if (token) {
      req = req.clone({ setHeaders: { 'X-XSRF-TOKEN': token } });
    }
  }

  return next(req);
};
