import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  {
  path: 'history',
  loadComponent: () =>
    import('./features/history/history.component').then(m => m.HistoryComponent)
  },
  {
  path: 'history',
  loadComponent: () =>
    import('./features/history/history.component').then(m => m.HistoryComponent)
}
];