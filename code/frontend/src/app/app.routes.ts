import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth.component';
import { HomeComponent } from './components/home/home.component';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';
import { roleGuard } from './guards/role.guard';
import { MapComponent } from './components/map/map.component';

export const routes: Routes = [
  {
    path: 'auth',
    component: AuthComponent,
  },

  {
    path: '',
    component: HomeComponent,
    canActivate: [ roleGuard(['CLIENT']) ],
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
    component: MapComponent,
  },

  {
    path: 'agent',
    component: AgentDashboardComponent,
    canActivate: [roleGuard(['AGENT'], '/')],
    children: [
      {
        path: 'add',
        loadComponent: () =>
          import('./components/ad/add-ad.component').then(
            (m) => m.AddAdComponent
          ),
      },
    ],
  },
];
