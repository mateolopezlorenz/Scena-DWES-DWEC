import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginResponse } from '../models';

@Injectable({
  providedIn: 'root',
})
export class Authservice {

  isLoggedIn: boolean;

  constructor(private http: HttpClient) {
    this.isLoggedIn = !!localStorage.getItem('token');
  }

  //Método que envía los datos del registro al backend a través de la petición y el endpoint.
  register(data: {username: string, email: string, password: string}) {
    return this.http.post<{message: string}>('api/auth/register', data);
  }

  //Método que envía los datos del login al backend a través de la petición y el endpoint.
  login(data: {username: string, password: string}) {
    return this.http.post<{message: string}>('api/auth/login', data);
  }

  //Método que envía los datos del evento registrado al backend.
  eventForm(data: {name: string, description: string, category: string, startDate: string, endDate: string, capacity: number, rooms: number}) {
    return this.http.post<{message: string}>('api/events/createEvent', data);
  }

  //Método que envía los datos del local registrado al backend.
  localForm(data: {name: string, latitude: number, longitude: number, ubication: string, capacity: number, rooms: number}) {
    return this.http.post<{message: string}>('api/locals/createLocal', data);
  }

  //Método para obtener el token JWT del localStorage.
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Método para comprobar si el usuario está autenticado.
  isAuthenticated(): boolean {
    return this.isLoggedIn;
  }

  //Método para poder hacer logout.
  logout(): void {
    localStorage.clear();
    this.isLoggedIn = false;
  }

  //Método que guarda la sesión del usuario.
  saveSession(response: LoginResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('username', response.username);
    localStorage.setItem('email', response.email);
    localStorage.setItem('id', response.id.toString());

    this.isLoggedIn = true;
  }
}
