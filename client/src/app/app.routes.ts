import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Registry } from './pages/registry/registry';
import { AuthGuard } from './guards/AuthGuard';

export const routes: Routes = [
  { path: 'home', component: Home, canActivate: [AuthGuard]},
  { path: 'login', component: Login },
  { path: 'registry', component: Registry },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home'
  },
  { path: '**', redirectTo: 'home' }
];
