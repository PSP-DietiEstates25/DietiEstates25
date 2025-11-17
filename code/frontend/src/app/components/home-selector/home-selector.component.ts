import { Component, computed, effect, inject, OnInit, Signal, signal } from '@angular/core';
import { LocalStorageService } from '../../services/services/local-storage.service';
import { HomeComponent } from '../home/home.component';
import { AgentDashboardComponent } from '../agent-dashboard/agent-dashboard.component';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';
import { Router } from '@angular/router';
import { single, windowTime } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home-selector',
  standalone: true,
  imports: [HomeComponent, AgentDashboardComponent, AdminDashboardComponent],
  templateUrl: './home-selector.component.html',
  styleUrl: './home-selector.component.scss'
})
export class HomeSelectorComponent implements OnInit {

  private readonly localStorageService = inject(LocalStorageService);
  private readonly authService = inject(AuthService);
  private readonly routerService = inject(Router);

  role!: string | null;

  isAuthenticated = signal<boolean>(false);
  _isAuthenticated!: Signal<boolean>;

  constructor(){
    effect(() => {
      this._isAuthenticated = computed(() => this.isAuthenticated());
    });
  }

  ngOnInit(): void {
    this.getUserInfo();
    this.role = this.authService.getRole();
  }

  getUserInfo(): void {
    this.authService.getUserInfo().subscribe({
      next: (userInfo) => {
        this.isAuthenticated.set(true);
        this.localStorageService.setItem("role", userInfo.role[0]);
      },
      error: (error) => {
        if (error?.status === 401) {
          this.isAuthenticated.set(false);
          this.login();
        }
      },
    });
  }

  login(): void {
    window.location.href = environment.loginUrl;
  }
}
