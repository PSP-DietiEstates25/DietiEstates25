import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('auth.token');

  const isApi = req.url.startsWith('/api') || req.url.includes('/api/');

  const isAuthEndpoint = /\/api\/auth\/(login|refresh|register)/.test(req.url);

  if (token && isApi && !isAuthEndpoint) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }
  return next(req);
};
