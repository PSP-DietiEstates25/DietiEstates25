import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { AgentDashboardComponent } from './features/agent/agent-dashboard/agent-dashboard.component';
import { AuthComponent } from './features/auth/auth.component';
import { AgentGuard } from './core/guards/agent.guard';
import { ClientGuard } from './core/guards/client.guard';

export const routes: Routes = [
  { path: 'auth', component: AuthComponent },

  {
    path: '',
    component: HomeComponent,
    canActivate: [ClientGuard],
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
    canActivate: [AgentGuard],
    /*
    children: [
      // quando vai su /agent (rotta vuota), mostri un componente “Pubblicati”
      { path: '', loadComponent: () => import('./features/agent-pubblicati/agent-pubblicati.component').then(m => m.AgentPubblicatiComponent) },

      // /agent/add -> componente per “Aggiungi annuncio”
      { path: 'add', loadComponent: () => import('./features/agent-add/agent-add.component').then(m => m.AgentAddComponent) },

      // /agent/visits -> componente “Visite prenotate”
      { path: 'visits', loadComponent: () => import('./features/agent-visits/agent-visits.component').then(m => m.AgentVisitsComponent) },

      // … puoi aggiungere altre sottorotte …
    ]
      */
  },
];
