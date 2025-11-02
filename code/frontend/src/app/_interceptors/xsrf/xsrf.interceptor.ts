import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';

export const xsrfInterceptor: HttpInterceptorFn = (req, next) => {
  if (['POST','PUT','PATCH','DELETE'].includes(req.method)) {
    const cookieService = inject(CookieService);
    const token = cookieService.get('XSRF-TOKEN');
    if (token) {
      req = req.clone({ setHeaders: { 'X-XSRF-TOKEN': token } });
    }
  }
  return next(req);
};
