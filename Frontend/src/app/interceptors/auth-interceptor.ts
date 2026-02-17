import { Injectable, Injector } from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor,} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private injector: Injector) {}

  //Método con el que interceptamos las peticiones para agregar el token de autenticación.
  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    //Obtenemos el token que se guarda al hacer el login.
    const token = localStorage.getItem('token');

    //Si el token existe, clonamos la petición y añadimos el header de autorización.
    if (token) {

      //Clonamos la petición.
      req = req.clone({

        //Definimos los headers.
        setHeaders: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
    }

    //Devolvemos la petición modificada con el token.
    return next.handle(req);
  }
}

