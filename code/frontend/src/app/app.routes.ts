import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth.component';
import { HomeComponent } from './components/home/home.component';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';
import { ClientGuard } from './guards/client.guard';
import { MapComponent } from './components/map/map.component';

export const routes: Routes = [
  {
    path: 'auth',
    component: AuthComponent,
  },

  {
    path: '',
    component: HomeComponent,
    //canActivate: [ClientGuard],
  },

  {
    path: 'history',
    loadComponent: () =>
      import('./components/history/history.component').then(
        (m) => m.HistoryComponent
      ),
  },

  {
  path: 'offer',
    loadComponent: () =>
      import('./components/offer-list/offer-list.component').then(
        (m) => m.OfferListComponent
      ),
  },

  {
    path: 'map',
    title: 'Mappa di prova',
    component: MapComponent

  },
  /*
  {
    path: 'notification',
    loadComponent: () =>
      import('./features/notification/notification.component').then(
        (m) => m.NotificationComponent
      ),
  },
*/

  {
    path: 'agent',
    component: AgentDashboardComponent,
    //canActivate: [AgentGuard],

    children: [
      // quando vai su /agent (rotta vuota), mostri un componente “Pubblicati”
      //{ path: '', loadComponent: () => import('./features/agent-pubblicati/agent-pubblicati.component').then(m => m.AgentPubblicatiComponent) },

      // /agent/add -> componente per “Aggiungi annuncio”
      {
        path: 'add',
        loadComponent: () =>
          import('./components/ad/add-ad.component').then(
            (m) => m.AddAdComponent
          ),
      },

      // /agent/visits -> componente “Visite prenotate”
      //{ path: 'visits', loadComponent: () => import('./features/agent-visits/agent-visits.component').then(m => m.AgentVisitsComponent) },

      // … puoi aggiungere altre sottorotte …
    ],
  },
];
