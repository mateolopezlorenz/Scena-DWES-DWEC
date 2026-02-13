import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class Authservice {
  
  constructor(private http: HttpClient) {}

  //Método que envía los datos del registro al backend a través de la petición y el endpoint.
  register(data: {username: string, email: string, password: string}) {
    return this.http.post<{message: string}>('api/auth/register', data);
  }

  login(data: {username: string, password: string}) {
    return this.http.post<{message: string}>('api/auth/login', data);
  }

}
