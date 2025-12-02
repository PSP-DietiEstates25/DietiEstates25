import { Component, computed, inject, OnInit, signal } from '@angular/core';
import {
  Router,
  NavigationEnd,
  RouterLink,
  RouterLinkActive,
} from '@angular/router';
import { CommonModule } from '@angular/common';
import { MenuToggleComponent } from '../../buttons/menu_toggle/menu-toggle.component';
import { NotificationsFacade } from '../../../components/notifications/notifications.facade';
import { AuthService } from '../../../manual_services/auth.service';
import { environment } from '../../../../environments/environment.development';
import { LocalStorageService } from '../../../manual_services/local-storage.service';

interface NavLink {
  label: string;
  path: string;
}

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, MenuToggleComponent],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  private readonly localStorageService = inject(LocalStorageService)
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  
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
    this.notifications.init();
  }

  ngOnInit(): void {
    this.getUserInfo();
  }

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
    this.authService.logout().subscribe(() => {
      this.isAuthenticated = false;
      this.email = '';

      this.localStorageService.removeItem('isAuthenticated');
      this.localStorageService.removeItem('role');

      this.router.navigateByUrl('/');
    });
  }


  getUserInfo(): void {
    this.authService.getUserInfo().subscribe({
      next: (userInfo) => {
        this.isAuthenticated = true;
        this.localStorageService.setItem("role", userInfo.role[0]);
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

  navLinks() {
    return this.allLinks;
  }
}
