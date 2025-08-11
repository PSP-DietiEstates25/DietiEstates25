import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { LoginRequest, LoginResponse } from '../core/auth.types';
import { firstValueFrom, BehaviorSubject, map } from 'rxjs';
import { TokenStorage } from './token-storage.service';
import { AppRole, mapBackendRoleToApp } from '../core/roles';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private storage = inject(TokenStorage);
  private base = environment.apiBaseUrl;

  private _userRole$ = new BehaviorSubject<AppRole | null>(null);
  userRole$ = this._userRole$.asObservable();

  private _email$ = new BehaviorSubject<string | null>(null);
  email$ = this._email$.asObservable();

  // nome visuale derivato dall'email
  displayName$ = this.email$.pipe(map((e) => (e ? e.split('@')[0] : '')));

  constructor() {
    // init da storage (token e role)
    const token = this.storage.token;
    const role = this.storage.role;
    if (role) this._userRole$.next(mapBackendRoleToApp(role));
    this._email$.next(this.extractEmailFromToken(token));
  }

  async login(email: string, password: string): Promise<LoginResponse> {
    const body: LoginRequest = { email, password };
    const res = await firstValueFrom(
      this.http.post<LoginResponse>(`${this.base}/api/auth/login`, body)
    );

    this.storage.set(res.token, res.role, res.subjectType);
    this._userRole$.next(mapBackendRoleToApp(res.role));
    this._email$.next(this.extractEmailFromToken(res.token));
    return res;
  }

  async register(email: string, password: string): Promise<void> {
    await firstValueFrom(
      this.http.post<void>(`${this.base}/api/auth/register`, {
        email,
        password,
      })
    );
  }

  logout() {
    this.storage.clear();
    this._userRole$.next(null);
    this._email$.next(null);
  }

  isAuthenticated(): boolean {
    return !!this.storage.token;
  }

  // helpers 
  private extractEmailFromToken(token: string | null): string | null {
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      // nel backend abbiamo messo l'email come subject (sub)
      return payload?.sub ?? null;
    } catch {
      return null;
    }
  }
}
