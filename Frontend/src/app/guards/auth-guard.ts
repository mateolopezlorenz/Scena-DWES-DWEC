import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { Authservice } from '../services/authservice';

// Guard serveix per protegir rutes que requereixen autenticació
// Impedeix que usuaris no autenticats accedeixin a /users redirige a /login.

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(Authservice) as Authservice;
  const router = inject(Router);

  if (auth.isAuthenticated()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
