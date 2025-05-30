import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { AgentDashboardComponent } from './features/agent-dashboard/agent-dashboard.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  
  {
    path: 'history',
    loadComponent: () =>
      import('./features/history/history.component').then(
        (m) => m.HistoryComponent
      ),
  },

  {
    path: 'history',
    loadComponent: () =>
      import('./features/history/history.component').then(
        (m) => m.HistoryComponent
      ),
  },

  {
    path: 'agent',
    component: AgentDashboardComponent,
  },
];
