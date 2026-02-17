import { Injectable, Injector } from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor,} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private injector: Injector) {}

  //Método con el que interceptamos las peticiones para agregar el token de autenticación.
  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {    
    const token = localStorage.getItem('token');
    console.log('Token disponible:', !!token);
    
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      console.log('Headers after clone:', req.headers.keys());
    }
    
    return next.handle(req);
  }
}

