// src/app/core/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface LoginResponse {
  token: string;
  role: 'CLIENT' | 'AGENT' | 'ADMIN';
  // username, userId, ecc.
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  // Un BehaviorSubject che tiene il ruolo corrente (o null se non loggato)
  private _userRole$ = new BehaviorSubject<'CLIENT' | 'AGENT' | 'ADMIN' | null>(null);

  constructor(private http: HttpClient) {
    // Al caricamento dell’app, prova a leggere lo “userRole” da localStorage
    const savedRole = localStorage.getItem('app-role') as any;
    if (savedRole) {
      this._userRole$.next(savedRole);
    }
  }

  // espone come observable la “role” dell’utente loggato
  get userRole$(): Observable<'CLIENT' | 'AGENT' | 'ADMIN' | null> {
    return this._userRole$.asObservable();
  }

  // Metodo di login (POST a /api/auth/login)
  loginWithEmail(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/api/auth/login', {
      email,
      password
    }).pipe(
      tap(resp => {
        // Quando riceviamo risposta valida, salva token e ruolo in localStorage
        localStorage.setItem('app-token', resp.token);
        localStorage.setItem('app-role', resp.role);
        // Aggiorna il BehaviorSubject
        this._userRole$.next(resp.role);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('app-token');
    localStorage.removeItem('app-role');
    this._userRole$.next(null);
  }

  // ... aggiungere metodi per login OAuth/Google/Facebook,
  // oppure per registrazione con email/password, modifica password, ecc
}
