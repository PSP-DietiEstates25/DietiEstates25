import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../vecchioService/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  imports: [RouterModule],
})
export class NavbarComponent {
  mobileMenuOpen: boolean = false;

  navLinks = [
    { path: '/', label: 'Home' },
    { path: '/notification', label: 'Notification' },
    { path: '/offer', label: 'Offer' },
    { path: '/history', label: 'History' },
    { path: '/pages', label: 'Visits' },
    { path: '/notices', label: 'Notices' },
  ];

  constructor(private authService: AuthService, private router: Router) {}

  toggleMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }
}
