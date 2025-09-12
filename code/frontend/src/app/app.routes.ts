import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth.component';
import { HomeComponent } from './components/home/home.component';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';
import { roleGuard } from './_guards/role.guard';
import { SearchPageComponent } from './components/search/search-page.component';

export const routes: Routes = [
  {
    path: 'auth',
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      {
        path: 'login',
        loadComponent: () =>
          import('../app/components/login/login.component').then(
            (m) => m.LoginComponent
          ),
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

  {
    path: '',
    component: HomeComponent,
    //canActivate: [ roleGuard(['CLIENT']) ],
  },
  /*
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
  
  */
  {
    path: 'search',
    component: SearchPageComponent,
  },

  {
    path: 'agent',
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./components/agent-dashboard/agent-dashboard.component').then(
            (m) => m.AgentDashboardComponent
          ),
      },
      {
        path: 'ads/new',
        loadComponent: () =>
          import('./components/create-ad/create-layout.component').then(
            (m) => m.AgentCreateLayoutComponent
          ),
        children: [
          { path: '', pathMatch: 'full', redirectTo: 'basics' },
          {
            path: 'basics',
            loadComponent: () =>
              import('./components/create-ad/step-basics.component').then(
                (m) => m.StepBasicsComponent
              ),
          },
          {
            path: 'details',
            loadComponent: () =>
              import('./components/create-ad/step-details.component').then(
                (m) => m.StepDetailsComponent
              ),
          },
          {
            path: 'photos',
            loadComponent: () =>
              import('./components/create-ad/step-photos.component').then(
                (m) => m.StepPhotosComponent
              ),
          },
          {
            path: 'review',
            loadComponent: () =>
              import('./components/create-ad/step-review.component').then(
                (m) => m.StepReviewComponent
              ),
          },
        ],
      },
    ],
  },
];
