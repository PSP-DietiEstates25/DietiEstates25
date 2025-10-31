import { Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';
import { roleGuard } from './_guards/role.guard';
import { SearchPageComponent } from './components/search/search-page.component';
import { CreateAdFacade } from './components/create-ad/create-ad.facade';
import { EditAdFacade } from './components/edit-ad/edit-ad.facade';
import { EditLayoutComponent } from './components/edit-ad/edit-layout.component';

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
            path: 'cadastral',
            loadComponent: () =>
              import('./components/create-ad/step-cadastral.component').then(
                (m) => m.StepCadastralComponent
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

      {
        path: 'ads/:realestateId/edit',
        component: EditLayoutComponent,
        providers: [
          EditAdFacade,
          { provide: CreateAdFacade, useExisting: EditAdFacade },
        ],
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
            path: 'cadastral',
            loadComponent: () =>
              import('./components/create-ad/step-cadastral.component').then(
                (m) => m.StepCadastralComponent
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

  {
    path: 'admin',
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./components/admin-dashboard/admin-dashboard.component').then(
            (m) => m.AdminDashboardComponent
          ),
      },

      {
        path: 'change-password',
        // canActivate: [roleGuard],
        data: { roles: ['ADMIN'] },
        loadComponent: () =>
          import(
            './components/admin-account/admin-change-password.component'
          ).then((m) => m.AdminChangePasswordComponent),
      },
    ],
  },

  {
    path: 'ad/:id',
    loadComponent: () =>
      import('./components/ad-detail/ad-detail.component').then(
        (m) => m.AdDetailComponent
      ),
  },

  {
    path: 'notifications',
    loadComponent: () =>
      import('./components/notifications/notifications-page.component').then(
        (m) => m.NotificationsPageComponent
      ),
    canActivate: [],
  },
];
