import { Injectable, computed, signal } from '@angular/core';

export type Role = 'CLIENT' | 'AGENT' | 'ADMIN';

function decodeJwt(token: string): any | null {
  try {
    const payload = token.split('.')[1];
    const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(json);
  } catch {
    return null;
  }
}

const ACCESS_TOKEN_KEY = 'accessToken';

@Injectable({ providedIn: 'root' })
export class AuthStore {
  private _token = signal<string | null>(
    localStorage.getItem(ACCESS_TOKEN_KEY)
  );

  readonly role = computed<Role | null>(() => {
    const t = this._token();
    if (!t) return null;
    const p = decodeJwt(t);
    if (!p) return null;

    // 1) es. "role": "ADMIN"
    if (typeof p.role === 'string') return p.role as Role;

    // 2) es. "roles": ["ROLE_ADMIN"] o ["ADMIN"]
    if (Array.isArray(p.roles) && p.roles.length) {
      const r = (p.roles as string[])[0].replace(/^ROLE_/, '');
      return (['ADMIN', 'AGENT', 'CLIENT'] as Role[]).includes(r as Role)
        ? (r as Role)
        : null;
    }

    // 3) es. "authorities": ["ROLE_AGENT", ...]
    if (Array.isArray(p.authorities) && p.authorities.length) {
      const r = (p.authorities as string[])[0].replace(/^ROLE_/, '');
      return (['ADMIN', 'AGENT', 'CLIENT'] as Role[]).includes(r as Role)
        ? (r as Role)
        : null;
    }

    return null;
  });

  readonly isAuthenticated = computed<boolean>(() => !!this._token());

  setToken(token: string) {
    localStorage.setItem(ACCESS_TOKEN_KEY, token);
    this._token.set(token);
  }

  clear() {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    this._token.set(null);
  }

  get token(): string | null {
    return this._token();
  }
}
