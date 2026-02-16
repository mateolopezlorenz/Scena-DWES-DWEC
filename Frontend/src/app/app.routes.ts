import { Routes } from '@angular/router';
import { Register } from './components/register/register';
import { Login } from './components/login/login';
import { EventForm } from './components/event-form/event-form';
import { Home } from './components/home/home';
import { EventList } from './components/event-list/event-list';
import { Event } from './components/event/event';
import { LocalForm } from './components/local-form/local-form';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'home', component: Home },
  { path: 'event-list', component: EventList },
  { path: 'event/:id', component: Event },
  { path: 'event', component: EventForm },
  { path: 'local', component: LocalForm },
  { path: '**', redirectTo: '/home' }
];
