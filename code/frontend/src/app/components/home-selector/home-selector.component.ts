import { Component, inject, OnInit } from '@angular/core';
import { LocalStorageService } from '../../manual_services/local-storage/local-storage.service';
import { HomeComponent } from '../home/home.component';
import { AgentDashboardComponent } from '../agent-dashboard/agent-dashboard.component';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-home-selector',
  standalone: true,
  imports: [HomeComponent, AgentDashboardComponent, AdminDashboardComponent],
  templateUrl: './home-selector.component.html',
  styleUrl: './home-selector.component.scss',
})
export class HomeSelectorComponent implements OnInit {
  private readonly localStorageService = inject(LocalStorageService);

  role!: string | null;

  ngOnInit(): void {
    const isAuthenticated =
      this.localStorageService.getItem('isAuthenticated') === 'true';
    const savedRole = this.localStorageService.getItem('role');

    if (!isAuthenticated) {
      this.login();
      return;
    }

    this.role = savedRole;
  }

  private login(): void {
    window.location.href = environment.loginUrl;
  }
}
