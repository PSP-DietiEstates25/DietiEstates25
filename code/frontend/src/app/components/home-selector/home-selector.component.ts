import {
  Component,
  computed,
  effect,
  inject,
  OnInit,
  Signal,
  signal,
} from '@angular/core';
import { LocalStorageService } from '../../manual_services/local-storage/local-storage.service';
import { HomeComponent } from '../home/home.component';
import { AgentDashboardComponent } from '../agent-dashboard/agent-dashboard.component';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';
import { Router } from '@angular/router';
import { single, windowTime } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../manual_services/auth/auth.service';
import { environmentMap } from '../../../environments/environment.map';

@Component({
  selector: 'app-home-selector',
  standalone: true,
  imports: [HomeComponent, AgentDashboardComponent, AdminDashboardComponent],
  templateUrl: './home-selector.component.html',
  styleUrl: './home-selector.component.scss',
})
export class HomeSelectorComponent implements OnInit {
  private readonly localStorageService = inject(LocalStorageService);
  private readonly authService = inject(AuthService);
  private readonly routerService = inject(Router);

  role!: string | null;

  ngOnInit(): void {
    const isAuthenticated =
      this.localStorageService.getItem('isAuthenticated') === 'true';
    const savedRole = this.localStorageService.getItem('role');

    if (!isAuthenticated) {
      // Non autenticato / ruolo non presente → vai subito al login
      this.login();
      return;
    }

    this.role = savedRole;
    // Niente chiamata a getUserInfo qui: è già stata fatta da AuthCallbackComponent
  }

  private login(): void {
    window.location.href = environment.loginUrl;
  }
}
