import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Authservice } from '../services/authservice';

// Utilizam el interceptor HTTP per afegir el token JWT a totes les sol·licitats 
// i així el servidor pot validar que l'usuari està autenticat
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(Authservice) as Authservice;
  const token = auth.getToken();

  // Si existeix un token, l'afegeix a la capçalera Authorization
  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(req);
};
