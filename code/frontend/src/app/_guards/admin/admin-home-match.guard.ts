import { inject } from '@angular/core';
import { CanMatchFn } from '@angular/router';
import { LocalStorageService } from '../../manual_services/local-storage/local-storage.service';
import { environment } from '../../../environments/environment';

export const adminHomeMatchGuard: CanMatchFn = () => {
  const localStorageService = inject(LocalStorageService);

  const isAuthenticated =
    localStorageService.getItem('isAuthenticated') === 'true';
  if (!isAuthenticated) {
    window.location.href = environment.loginUrl;
    return false;
  }

  return localStorageService.getItem('role') === 'ADMIN';
};
