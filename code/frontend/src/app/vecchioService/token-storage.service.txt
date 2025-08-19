import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth.token';
const ROLE_KEY = 'auth.role';
const SUBJ_KEY = 'auth.subjectType';

@Injectable({ providedIn: 'root' })
export class TokenStorage {
  set(token: string, role: string, subjectType: string) {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(ROLE_KEY, role);
    localStorage.setItem(SUBJ_KEY, subjectType);
  }
  get token(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }
  get role(): string | null {
    return localStorage.getItem(ROLE_KEY);
  }
  get subjectType(): string | null {
    return localStorage.getItem(SUBJ_KEY);
  }
  clear() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
    localStorage.removeItem(SUBJ_KEY);
  }
}
