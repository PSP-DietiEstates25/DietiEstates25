import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth.component';
import { HomeComponent } from './components/home/home.component';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';
import { roleGuard } from './_guards/role.guard';
import { MapComponent } from './components/map/map.component';
import { SearchPageComponent } from './components/search/search-page.component';

export const routes: Routes = [
  {
    path: 'auth',
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      {
        path: 'login',
        loadComponent: () =>
          import('../app/components/login/login.component').then((m) => m.LoginComponent),
      },
      {
        path: 'register',
        loadComponent: () =>
          import('../app/components/register/register.component').then(
            (m) => m.RegisterComponent
          ),
      },
    ],
  },

  /*
  {
    path: 'agent',
    loadComponent: () =>
      import('./pages/agent/agent.component').then((m) => m.AgentComponent),
    canMatch: [
      () => import('./vecchioService/auth/auth.guard').then((m) => m.roleGuard),
    ],
    data: { requiredRole: 'AGENT' },
  },
  */

  {
    path: '',
    component: HomeComponent,
    //canActivate: [ roleGuard(['CLIENT']) ],
  },

  {
    path: 'history',
    loadComponent: () =>
      import('./components/history/history.component').then(
        (m) => m.HistoryComponent
      ),
  },
  /*
  {
    path: 'offer',
    loadComponent: () =>
      import('./components/offer-list/offer-list.component').then(
        (m) => m.OfferListComponent
      ),
  },
*/
  {
    path: 'map',
    title: 'Mappa di prova',
    component: MapComponent,
  },

  {
    path: 'search',
    component: SearchPageComponent,
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
