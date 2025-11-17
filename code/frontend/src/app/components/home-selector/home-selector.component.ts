import { Component, inject } from '@angular/core';
import { LocalStorageService } from '../../services/services/local-storage.service';
import { HomeComponent } from '../home/home.component';
import { AgentDashboardComponent } from '../agent-dashboard/agent-dashboard.component';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';

@Component({
  selector: 'app-home-selector',
  imports: [HomeComponent, AgentDashboardComponent, AdminDashboardComponent],
  templateUrl: './home-selector.component.html',
  styleUrl: './home-selector.component.scss'
})
export class HomeSelectorComponent {

  private readonly localStorageService = inject(LocalStorageService);

  role!: string;

  constructor(){
    this.localStorageService.getItem("role");
  }
}
