import { Routes } from '@angular/router';

import { isUserGuard } from './_guards/user/is-user.guard';
import { isEstateAgentGuard } from './_guards/estate-agent/is-estate-agent.guard';
import { isEstateAgentOrAdminGuard } from './_guards/admin-or-agent/is-estate-agent-or-admin.guard';

import { adminHomeMatchGuard } from './_guards/admin/admin-home-match.guard';
import { agentHomeMatchGuard } from './_guards/agent/agent-home-match.guard';

export const routes: Routes = [
  // AUTH (lazy)
  {
    path: 'auth',
    children: [
      {
        path: 'register',
        loadComponent: () =>
          import('./components/register/register.component').then(
            (m) => m.RegisterComponent,
          ),
      },
      {
        path: 'callback',
        loadComponent: () =>
          import('./components/auth-callback/auth-callback.component').then(
            (m) => m.AuthCallbackComponent,
          ),
      },
    ],
  },

  // HOME sempre "/"
  {
    path: '',
    pathMatch: 'full',
    canMatch: [adminHomeMatchGuard],
    loadComponent: () =>
      import('./components/admin-dashboard/admin-dashboard.component').then(
        (m) => m.AdminDashboardComponent,
      ),
  },
  {
    path: '',
    pathMatch: 'full',
    canMatch: [agentHomeMatchGuard],
    loadComponent: () =>
      import('./components/agent-dashboard/agent-dashboard.component').then(
        (m) => m.AgentDashboardComponent,
      ),
  },
  {
    path: '',
    pathMatch: 'full',
    loadComponent: () =>
      import('./components/home/home.component').then((m) => m.HomeComponent),
  },

  // Pagine normali (tutte lazy)
  {
    path: 'searches',
    title: 'Searches',
    loadComponent: () =>
      import('./components/search/search-page.component').then(
        (m) => m.SearchPageComponent,
      ),
  },
  {
    path: 'search',
    title: 'Search',
    loadComponent: () =>
      import(
        './components/search-landing-map/search-landing-map.component'
      ).then((m) => m.SearchLandingMapComponent),
  },
  {
    path: 'offers',
    title: 'Offers',
    canActivate: [isUserGuard],
    loadComponent: () =>
      import('./components/offer-page/offers-page.component').then(
        (m) => m.OffersPageComponent,
      ),
  },
  {
    path: 'notifications',
    canActivate: [isUserGuard],
    loadComponent: () =>
      import('./components/notifications/notifications-page.component').then(
        (m) => m.NotificationsPageComponent,
      ),
  },
  {
    path: 'ad/:id',
    loadComponent: () =>
      import('./components/ad-detail/ad-detail.component').then(
        (m) => m.AdDetailComponent,
      ),
  },

  // CREATE (mantieni i tuoi URL /basics /details /photos ecc)
  // IMPORTANT: route messa DOPO tutte le altre per evitare interferenze
  {
    path: '',
    canActivate: [isEstateAgentGuard],
    loadComponent: () =>
      import('./components/create-ad/create-layout.component').then(
        (m) => m.AgentCreateLayoutComponent,
      ),
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'basics' },
      {
        path: 'basics',
        loadComponent: () =>
          import('./components/create-ad/step-basics.component').then(
            (m) => m.StepBasicsComponent,
          ),
      },
      {
        path: 'details',
        loadComponent: () =>
          import('./components/create-ad/step-details.component').then(
            (m) => m.StepDetailsComponent,
          ),
      },
      {
        path: 'cadastraldata',
        loadComponent: () =>
          import('./components/create-ad/step-cadastral.component').then(
            (m) => m.StepCadastralComponent,
          ),
      },
      {
        path: 'photos',
        loadComponent: () =>
          import('./components/create-ad/step-photos.component').then(
            (m) => m.StepPhotosComponent,
          ),
      },
      {
        path: 'review',
        loadComponent: () =>
          import('./components/create-ad/step-review.component').then(
            (m) => m.StepReviewComponent,
          ),
      },
    ],
  },

  // EDIT (lazy + niente providers in route)
  {
    path: 'edit/:realestateId',
    canActivate: [isEstateAgentOrAdminGuard],
    loadComponent: () =>
      import('./components/edit-ad/edit-layout.component').then(
        (m) => m.EditLayoutComponent,
      ),
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'basics' },
      {
        path: 'basics',
        loadComponent: () =>
          import('./components/create-ad/step-basics.component').then(
            (m) => m.StepBasicsComponent,
          ),
      },
      {
        path: 'details',
        loadComponent: () =>
          import('./components/create-ad/step-details.component').then(
            (m) => m.StepDetailsComponent,
          ),
      },
      {
        path: 'cadastraldata',
        loadComponent: () =>
          import('./components/create-ad/step-cadastral.component').then(
            (m) => m.StepCadastralComponent,
          ),
      },
      {
        path: 'photos',
        loadComponent: () =>
          import('./components/create-ad/step-photos.component').then(
            (m) => m.StepPhotosComponent,
          ),
      },
      {
        path: 'review',
        loadComponent: () =>
          import('./components/create-ad/step-review.component').then(
            (m) => m.StepReviewComponent,
          ),
      },
    ],
  },

  { path: '**', redirectTo: '' },
];
