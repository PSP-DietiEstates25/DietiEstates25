import { Routes } from '@angular/router';

import { SearchPageComponent } from './components/search/search-page.component';
import { CreateAdFacade } from './components/create-ad/create-ad.facade';
import { EditAdFacade } from './components/edit-ad/edit-ad.facade';
import { EditLayoutComponent } from './components/edit-ad/edit-layout.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeSelectorComponent } from './components/home-selector/home-selector.component';
import { DetailControllerService } from './services/services';
import { StepDetailsComponent } from './components/create-ad/step-details.component';
import { StepCadastralComponent } from './components/create-ad/step-cadastral.component';
import { StepBasicsComponent } from './components/create-ad/step-basics.component';
import { isUserGuard } from './_guards/user/is-user.guard';
import { isEstateAgentGuard } from './_guards/estate-agent/is-estate-agent.guard';
import { StepPhotosComponent } from './components/create-ad/step-photos.component';
import { StepReviewComponent } from './components/create-ad/step-review.component';
import { AgentCreateLayoutComponent } from './components/create-ad/create-layout.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { AdDetailComponent } from './components/ad-detail/ad-detail.component';
import { NotificationsPageComponent } from './components/notifications/notifications-page.component';
import { isAdminGuard } from './_guards/admin/is-admin.guard';
import { AuthCallbackComponent } from './components/auth-callback/auth-callback.component';

export const routes: Routes = [
  {
    path: 'auth',
    children: [
      {
        path: 'register',
        component: RegisterComponent,
      },
      {
        path: 'callback',
        component: AuthCallbackComponent,
      },
    ],
  },
  {
    path: '',
    title: 'Home',
    pathMatch: 'full',
    component: HomeSelectorComponent,
  },
  {
    path: 'search',
    component: SearchPageComponent,
  },
  {
    path: '',
    canActivate: [isEstateAgentGuard],
    component: AgentCreateLayoutComponent,
    children: [
      {
        path: '',
        title: 'Publishing',
        pathMatch: 'full',
        redirectTo: 'basics',
      },
      {
        path: 'basics',
        title: 'Basics info step',
        component: StepBasicsComponent,
      },
      {
        path: 'details',
        title: 'Details step',
        component: StepDetailsComponent,
      },
      {
        path: 'cadastraldata',
        title: 'Cadastral data step',
        component: StepCadastralComponent,
      },
      {
        path: 'photos',
        title: 'Photos step',
        component: StepPhotosComponent,
      },
      {
        path: 'review',
        title: 'Review',
        component: StepReviewComponent,
      },
    ],
  },
  
  {
    path: 'edit/:realestateId',
    canActivate: [isEstateAgentGuard],
    component: EditLayoutComponent,
    providers: [
      EditAdFacade,
      { provide: CreateAdFacade, useExisting: EditAdFacade },
    ],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'basics' },
      { path: 'basics', component: StepBasicsComponent },
      { path: 'details', component: StepDetailsComponent },
      { path: 'cadastraldata', component: StepCadastralComponent },
      { path: 'photos', component: StepPhotosComponent },
      { path: 'review', component: StepReviewComponent },
    ],
  },

  {
    path: 'ad/:id',
    component: AdDetailComponent,
  },

  {
    path: 'notifications',
    component: NotificationsPageComponent,
    canActivate: [isUserGuard],
  },
];

/*

  ROUTES INIZIALI CON COMPONENTI NON LAZY

  {
    path: '',
    canActivate: [isEstateAgentGuard],
    children: [
      {
        path: '',
        component: AgentCreateLayoutComponent,
        children: [
          { 
            path: '',
            title: 'Publishing',
            pathMatch: 'full',
            redirectTo: 'basics'
          },
          {
            path: 'basics',
            title: 'Basics info step',
            component: StepBasicsComponent
          },
          {
            path: 'details',
            title: 'Details step',
            component: StepDetailsComponent
          },
          {
            path: 'cadastraldata',
            title: 'Cadastral data step',
            component: StepCadastralComponent 
          },
          {
            path: 'photos',
            title: 'Photos step',
            component: StepPhotosComponent
          },
          {
            path: 'reviews',
            title: 'Review',
            component: StepReviewComponent
          },
        ],
      },
      
      {
        path: ':realestateId',
        component: EditLayoutComponent,
        providers: [
          EditAdFacade,
          { provide: CreateAdFacade, useExisting: EditAdFacade },
        ],
        children: [
          { 
            path: '',
            pathMatch: 'full',
            redirectTo: 'basics'
          },
          {
            path: 'basics',
            component: StepBasicsComponent
          },
          {
            path: 'details',
            component: StepDetailsComponent
          },
          {
            path: 'cadastraldata',
            component: StepCadastralComponent
          },
          {
            path: 'photos',
            component: StepPhotosComponent
          },
          {
            path: 'reviews',
            component: StepReviewComponent
          },
        ],
      },
    ],
  },
*/

/*

ROUTES INIZIALI CON COMPONENTI LAZY

export const routes: Routes = [
  {
    path: 'auth',
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
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
*/
