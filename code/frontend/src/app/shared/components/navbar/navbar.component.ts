import { Component, effect, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MenuToggleComponent } from '../../buttons/menu_toggle/menu-toggle.component';
import { environment } from '../../../../environments/environment';
import { AutentServiceService } from '../../../autent.service.service';

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
export class NavbarComponent implements OnInit {

  private autent = inject(AutentServiceService);
  isMenuOpen = false;
  isAuthenticated = false;
  email = '';

  // link base
  navLinks: NavLink[] = [
    { label: 'Home', path: '/' },
    { label: 'Ricerca', path: '/search' },
    { label: 'Mappa', path: '/map' },
  ];

  private router = inject(Router);

  ngOnInit(): void {
    this.getUserInfo();
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }
  
  closeMenu() {
    this.isMenuOpen = false;
  }

  onClickLogin(){
    this.closeMenu();
    // The Backend is configured to trigger login when unauthenticated
    window.location.href =
    `${environment.apiBaseUrl}/oauth2/authorization/messaging-client-oidc?prompt=login`;
  }

  onClickRegister(){}

  /* VECCHIO LOGOUT
  logout() {
    this.auth.logout();
    this.closeMenu();
    clearStorage();
    this.router.navigateByUrl('/auth');
  }
  */

  logout(): void {
    this.closeMenu();
    this.autent.logout()
      .subscribe(() => {
        this.isAuthenticated = false;
        this.email = '';
        this.router.navigateByUrl('/')
      });
  }

  getUserInfo(): void {
  this.autent.getUserInfo().subscribe({
    next: (userInfo) => {
      this.isAuthenticated = true;
      this.email = userInfo.email ?? userInfo.preferred_username ?? userInfo.sub ?? '';
    },
    error: (err) => {
      if (err?.status === 401) {
        this.isAuthenticated = false;
        this.email = '';
        return;
      }
    }
  });
}
}