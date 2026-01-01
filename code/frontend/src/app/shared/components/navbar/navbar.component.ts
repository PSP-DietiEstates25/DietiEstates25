import { Component, computed, inject, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MenuToggleComponent } from '../../buttons/menu_toggle/menu-toggle.component';
import { NotificationsFacade } from '../../../components/notifications/notifications.facade';
import { AuthService } from '../../../manual_services/auth/auth.service';
import { environment } from '../../../../environments/environment';
import { LocalStorageService } from '../../../manual_services/local-storage/local-storage.service';

interface NavLink {
  label: string;
  path: string;
}

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MenuToggleComponent],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {
  private readonly localStorageService = inject(LocalStorageService);
  private readonly authService = inject(AuthService);

  readonly notifications = inject(NotificationsFacade);

  isMenuOpen = false;
  isAuthenticated = false;
  email = '';

  readonly notifBadge = computed(() => this.notifications.unreadCount());

  readonly allLinks: NavLink[] = [
    { label: 'Home', path: '/' },
    { label: 'Ricerca', path: '/searches' },
    { label: 'Le mie offerte', path: '/offers' },
    { label: 'Notifiche', path: '/notifications' },
  ];

  constructor() {
    this.notifications.fetchBadgeData();
  }

  /*
  ngOnInit(): void {
    this.getUserInfo();
  }
    */

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu() {
    this.isMenuOpen = false;
  }

  onClickLogin(): void {
    this.closeMenu();
    window.location.href = environment.loginUrl;
  }

  logout(): void {
    this.closeMenu();
    this.authService.logoutAndRedirectToLogin();
  }

  /*
  getUserInfo(): void {
    this.authService.getUserInfo().subscribe({
      next: (userInfo) => {
        this.isAuthenticated = true;
        this.localStorageService.setItem('role', userInfo.role[0]);
        this.email = userInfo.sub;
      },
      error: (error) => {
        if (error?.status === 401) {
          this.isAuthenticated = false;
          this.email = '';
          return;
        }
      },
    });
  }
  */

  navLinks() {
    return this.allLinks;
  }
}
