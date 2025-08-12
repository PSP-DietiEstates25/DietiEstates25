import { Injectable, inject, signal } from '@angular/core';
import { LocalStorageService } from '../local-storage/local-storage.service';
import { AuthState } from '../../interfaces/auth/auth-state';

/*

*/

@Injectable({ providedIn: 'root' })
export class AuthService {
  
  localStorageService = inject(LocalStorageService);

  authState: WriteableSignal<AuthState> = signal<AuthState>({
    email: this.getEmail(),
    token: this.getToken(),
    isAuthenticated: this.isAuthenticated(),
  });

  getEmail(){
    return this.localStorageService.getItem('email');
  }

  getToken(){
    return this.localStorageService.getItem('token');
  }

  isAuthenticated(){}
}
