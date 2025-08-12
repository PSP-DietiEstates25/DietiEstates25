import { Injectable, inject, signal, WritableSignal, computed, effect } from '@angular/core';
import { LocalStorageService } from '../local-storage/local-storage.service';
import { AuthState } from '../../interfaces/auth/auth-state';
import { jwtDecode } from 'jwt-decode';

/*

*/

@Injectable({ providedIn: 'root' })
export class AuthService {
  
  localStorageService = inject(LocalStorageService);

  authState: WritableSignal<AuthState> = signal<AuthState>({
    email: this.getEmail(),
    token: this.getToken(),
    isAuthenticated: this.isUserAuthenticated(),
  });

  username = computed(() => this.authState().email);
  token = computed(() => this.authState().token);
  isAuthenticated = computed(() => this.authState().isAuthenticated);

  constructor(){
    effect( () => {
      const token = this.authState().token;
      const email = this.authState().email;
      if(token !== null){
        this.localStorageService.setItem("token", token);
      } else {
        this.localStorageService.removeItem("token");
      }
      if(email !== null){
        this.localStorageService.setItem("email", email);
      } else {
        this.localStorageService.removeItem("email");
      }
    });
  }

  getEmail(){
    return this.localStorageService.getItem('email');
  }

  getToken(){
    return this.localStorageService.getItem('token');
  }

  isUserAuthenticated(): boolean {
    return this.verifyToken(this.getToken());
  }

  async updateToken(token: string) {
    const decodedToken: any = jwtDecode(token);
    const email = decodedToken.email;
    this.authState.set({
      email: email,
      token: token,
      isAuthenticated: this.verifyToken(token)
    })
  }

  verifyToken(token: string | null): boolean {
    if(token !== null){
      try{
        const decodedToken = jwtDecode(token);
        const expiration = decodedToken.exp;
        if(expiration === undefined || Date.now() >= expiration * 1000){
          return false;
        } else {
          return true;
        }
      } catch(error) {
        return false;
      }
    }
    return false;
  }

  logout(){
    this.authState.set({
      email: null,
      token: null,
      isAuthenticated: false
    });
  }
}
