import { Component, effect, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../vecchioService/auth/auth.service';
import { MenuToggleComponent } from '../../buttons/menu_toggle/menu-toggle.component';
import { OAuthService } from 'angular-oauth2-oidc';

interface NavLink {
  label: string;
  path: string;
}

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  imports: [CommonModule, RouterLink, RouterLinkActive, MenuToggleComponent],
})
export class NavbarComponent {

  private oauthService = inject(OAuthService);

  isMenuOpen = false;
  isAuthenticated = false;
  role: 'ADMIN' | 'AGENT' | 'CLIENT' | null = null;
  displayName = '';

  // link base
  navLinks: NavLink[] = [
    { label: 'Home', path: '/' },
    { label: 'Ricerca', path: '/search' },
    { label: 'Mappa', path: '/map' },
  ];

  private auth = inject(AuthService);
  private router = inject(Router);

  constructor() {
    effect(() => {
      const state = this.auth.authState();
      this.isAuthenticated = !!state.isAuthenticated;
      const email = state.email;
    });

    effect(() => {
      try {
        this.role = this.auth.role ? this.auth.role() : null;
      } catch {
        this.role = null;
      }
    });
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }
  
  closeMenu() {
    this.isMenuOpen = false;
  }

  onClickLogin(){
    this.closeMenu();
    this.oauthService.initCodeFlow();
  }

  onClickRegister(){}

  logout() {
    this.auth.logout();
    this.closeMenu();
    clearStorage();
    this.router.navigateByUrl('/auth');
  }
}

function clearStorage() {
  localStorage.removeItem('userEmail');
  localStorage.removeItem('userRole');
  localStorage.removeItem('isAuthenticated');
}
