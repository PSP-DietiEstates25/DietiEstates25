import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { LocalStorageService } from '../local-storage/local-storage.service';
import { tap } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';

export type Role = 'ADMIN' | 'AGENT' | 'CLIENT';

export interface LoginResponse {
  token: string;
  role?: Role;
  email?: string;
  userId?: string;
}

export interface RegisterPayload {
  name: string;
  surname?: string;
  email: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private ls = inject(LocalStorageService);
  private router = inject(Router);

  private readonly TOKEN_KEY = 'auth.token';

  // token in memoria
  private _token = signal<string | null>(this.ls.getItem(this.TOKEN_KEY));

  // payload decodificato (best-effort, NON validazione crittografica)
  private _payload = computed(() => {
    const t = this._token();
    if (!t) return null;
    try {
      const payload = JSON.parse(atob(t.split('.')[1] || ''));
      return payload ?? null;
    } catch {
      return null;
    }
  });

    private _userRole$ = new BehaviorSubject<'CLIENT' | 'AGENT' | 'ADMIN' | null>(null);
  public get userRole$(): Observable<'CLIENT' | 'AGENT' | 'ADMIN' | null> {
    return this._userRole$.asObservable();
  }

  authState = computed(() => {
    const payload = this._payload();
    return {
      isAuthenticated: !!this._token(),
      email: payload?.email ?? payload?.sub ?? null,
      userId: payload?.userId ?? null,
    } as const;
  });

  role = computed<Role | null>(() => {
    const p = this._payload();
    if (!p) return null;
    // prova vari campi comuni
    return (
      (p.role as Role) ||
      (Array.isArray(p.roles) ? (p.roles[0] as Role) : null) ||
      null
    );
  });

  get token(): string | null {
    return this._token();
  }

  setToken(t: string | null) {
    this._token.set(t);
    if (t) this.ls.setItem(this.TOKEN_KEY, t);
    else this.ls.removeItem(this.TOKEN_KEY);
  }

  login(email: string, password: string) {
    return this.http
      .post<LoginResponse>('/api/auth/login', { email, password })
      .pipe(
        tap((res) => {
          if (res?.token) this.setToken(res.token);
        })
      );
  }

  register(payload: RegisterPayload) {
    return this.http
      .post<Partial<LoginResponse>>('/api/auth/register', payload)
      .pipe(
        tap((res) => {
          if (res?.token) this.setToken(res.token);
        })
      );
  }

  logout() {
    this.setToken(null);
    this.router.navigateByUrl('/auth');
  }
}
