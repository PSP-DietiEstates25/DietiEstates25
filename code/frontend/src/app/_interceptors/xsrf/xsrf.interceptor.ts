import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { environment } from '../../../environments/environment';

function isAbsoluteUrl(url: string): boolean {
  return /^https?:\/\//i.test(url);
}

function normalizeBase(base: string): string {
  if (!base) return '';
  return base.endsWith('/') && base !== '/' ? base.slice(0, -1) : base;
}

const API_BASE = normalizeBase(environment.apiBaseUrl);

export const xsrfInterceptor: HttpInterceptorFn = (req, next) => {
  const isAbsReq = isAbsoluteUrl(req.url);

  const hitsBff = (() => {
    if (isAbsReq) {
      return false;
    }

    if (!API_BASE || API_BASE === '/') return req.url.startsWith('/');
    if (!isAbsoluteUrl(API_BASE) && API_BASE.startsWith('/')) {
      return req.url === API_BASE || req.url.startsWith(API_BASE + '/');
    }
    return req.url.startsWith('/');
  })();

  if (hitsBff && ['POST', 'PUT', 'PATCH', 'DELETE'].includes(req.method)) {
    const token = inject(CookieService).get('XSRF-TOKEN');
    if (token) {
      req = req.clone({ setHeaders: { 'X-XSRF-TOKEN': token } });
    }
  }

  return next(req);
};
