import { Component, computed, inject, signal } from '@angular/core';
import {
  Router,
  NavigationEnd,
  RouterLink,
  RouterLinkActive,
} from '@angular/router';
import { CommonModule } from '@angular/common';
import { MenuToggleComponent } from '../../buttons/menu_toggle/menu-toggle.component';
import { NotificationsFacade } from '../../../components/notifications/notifications.facade';
import { filter } from 'rxjs/operators';

type Role = 'ADMIN' | 'AGENT' | 'CLIENT';
interface NavLink {
  label: string;
  path: string;
  showIf?: (ctx: Ctx) => boolean;
}
type Ctx = { isAuthenticated: boolean; role: Role | null };

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, MenuToggleComponent],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {
  isMenuOpen = signal(false);

  isAuthenticated = signal(false);
  role = signal<Role | null>(null);
  displayName = signal<string>('');

  private readonly router = inject(Router);
  private readonly notifications = inject(NotificationsFacade);

  readonly notifBadge = computed(() => this.notifications.unreadCount());

  private readonly allLinks: NavLink[] = [
    { label: 'Home', path: '/' },
    { label: 'Ricerca', path: '/search' },
    {
      label: 'Dashboard',
      path: '/dashboard',
      showIf: (c) => c.isAuthenticated && !!c.role,
    },
    {
      label: 'Notifiche',
      path: '/notifications',
      showIf: (c) => c.isAuthenticated,
    },
  ];
  readonly navLinks = computed<NavLink[]>(() => {
    const ctx: Ctx = {
      isAuthenticated: this.isAuthenticated(),
      role: this.role(),
    };
    return this.allLinks.filter((l) => !l.showIf || l.showIf(ctx));
  });

  constructor() {
    this.notifications.init();

    this.readAuthFromStorage();

    this.router.events
      .pipe(filter((e) => e instanceof NavigationEnd))
      .subscribe(() => this.isMenuOpen.set(false));

    window.addEventListener('storage', (ev) => {
      if (['access_token', 'userEmail', 'userRole'].includes(ev.key ?? '')) {
        this.readAuthFromStorage();
      }
    });
  }

  toggleMenu() {
    this.isMenuOpen.update((v) => !v);
  }
  closeMenu() {
    this.isMenuOpen.set(false);
  }

  logout() {
    this.clearStorage();
    this.readAuthFromStorage(); 
    this.router.navigateByUrl('/auth');
  }


  private readAuthFromStorage() {
    const token = localStorage.getItem('access_token') ?? '';
    const decoded = this.safeDecodeJwt(token);
    const email = (
      decoded?.email ??
      decoded?.sub ??
      localStorage.getItem('userEmail') ??
      ''
    ).toString();
    const role = (decoded?.role ??
      localStorage.getItem('userRole') ??
      null) as Role | null;

    if (token && decoded) {
      this.isAuthenticated.set(true);
      this.displayName.set(email || '');
      this.role.set(role);
      return;
    }

    if (email || role) {
      this.isAuthenticated.set(true);
      this.displayName.set(email);
      this.role.set(role);
      return;
    }

    this.isAuthenticated.set(false);
    this.displayName.set('');
    this.role.set(null);
  }

  private safeDecodeJwt(token: string): any | null {
    try {
      if (!token || token.split('.').length !== 3) return null;
      const payload = token.split('.')[1];
      const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
      const json = atob(base64);
      return JSON.parse(json);
    } catch {
      return null;
    }
  }

  private clearStorage() {
    try {
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
      localStorage.removeItem('userEmail');
      localStorage.removeItem('userRole');
      localStorage.removeItem('isAuthenticated');
    } catch {}
  }
}
