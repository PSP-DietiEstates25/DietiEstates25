import { Routes } from '@angular/router';
import { AuthComponent } from './features/auth/auth.component';
import { HomeComponent } from './features/home/home.component';
import { AgentDashboardComponent } from './features/agent/agent-dashboard/agent-dashboard.component';
import { AddAdComponent } from './features/agent/add-ad/add-ad.component';
import { AgentGuard } from './core/guards/agent.guard';
import { ClientGuard } from './core/guards/client.guard';

export const routes: Routes = [
  { path: 'auth', component: AuthComponent },

  {
    path: '',
    component: HomeComponent,
    //canActivate: [ClientGuard],
  },

  {
    path: 'history',
    loadComponent: () =>
      import('./features/history/history.component').then(
        (m) => m.HistoryComponent
      ),
  },

  {
    path: 'notification',
    loadComponent: () =>
      import('./features/notification/notification.component').then(
        (m) => m.NotificationComponent
      ),
  },

  {
    path: 'agent',
    component: AgentDashboardComponent,
    //canActivate: [AgentGuard],
  
    children: [
      // quando vai su /agent (rotta vuota), mostri un componente “Pubblicati”
      //{ path: '', loadComponent: () => import('./features/agent-pubblicati/agent-pubblicati.component').then(m => m.AgentPubblicatiComponent) },

      // /agent/add -> componente per “Aggiungi annuncio”
      { path: 'add', loadComponent: () => import('./features/agent/add-ad/add-ad.component').then(m => m.AddAdComponent) },

      // /agent/visits -> componente “Visite prenotate”
      //{ path: 'visits', loadComponent: () => import('./features/agent-visits/agent-visits.component').then(m => m.AgentVisitsComponent) },

      // … puoi aggiungere altre sottorotte …
    ]
  },
];
