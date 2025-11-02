import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import {
  provideHttpClient,
  withInterceptors,
  withFetch,
} from '@angular/common/http';
import { routes } from './app.routes';
import { xsrfInterceptor } from './_interceptors/xsrf/xsrf.interceptor';
import { authInterceptor } from './_interceptors/auth/auth.interceptor'; 
import { authCredentials } from './_interceptors/cookie/auth-credentials.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(
      withFetch(),
      withInterceptors([xsrfInterceptor, authInterceptor, authCredentials])
    ),
  ],
};
