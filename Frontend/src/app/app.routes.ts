import { Routes } from '@angular/router';
import { Register } from './components/register/register';
import { Login } from './components/login/login';
import { EventForm } from './components/event-form/event-form';
import { Home } from './components/home/home';

export const routes: Routes = [
  { path: '', redirectTo: '/register', pathMatch: 'full' }, 
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'event', component: EventForm },
  { path: 'home', component: Home},
  { path: '**', redirectTo: '/register' } 
];
