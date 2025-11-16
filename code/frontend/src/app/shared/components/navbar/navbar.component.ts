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
import { AutentServiceService } from '../../../autent.service.service';
import { filter } from 'rxjs/operators';
import { environment } from '../../../../environments/environment.development';
import { LocalStorageService } from '../../../services/services/local-storage.service';

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
  private readonly autent = inject(AutentServiceService);
  private readonly router = inject(Router);
  private readonly notifications = inject(NotificationsFacade);

  isMenuOpen = false;
  isAuthenticated = false;
  email = '';

  readonly notifBadge = computed(() => this.notifications.unreadCount());

  readonly allLinks: NavLink[] = [
    { label: 'Home', path: '/' },
    { label: 'Ricerca', path: '/search' },
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
    window.location.href = `${environment.apiBaseUrl}/oauth2/authorization/messaging-client-oidc?prompt=login`;
  }

  onClickRegister(): void {}

  logout(): void {
    this.closeMenu();
    this.autent.logout().subscribe(() => {
      this.isAuthenticated = false;
      this.email = '';
      this.localStorageService.clear();
      this.router.navigateByUrl('/');
    });
  }

  getUserInfo(): void {
    this.autent.getUserInfo().subscribe({
      next: (userInfo) => {
        this.isAuthenticated = true;
        this.localStorageService.setItem("role", userInfo.role[0]);
        this.email = userInfo.sub;
      },
      error: (err) => {
        if (err?.status === 401) {
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
