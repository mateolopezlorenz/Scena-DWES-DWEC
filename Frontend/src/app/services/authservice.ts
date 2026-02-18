import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginResponse } from '../models';

@Injectable({
  providedIn: 'root',
})
export class Authservice {

  isLoggedIn: boolean;
  private url = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {
    this.isLoggedIn = !!localStorage.getItem('token');
  }

  //Método que envía los datos del registro al backend a través de la petición y el endpoint.
  register(data: {username: string, email: string, password: string}) {
    return this.http.post<{message: string}>(`${this.url}/register`, data);
  }

  //Método que envía los datos del login al backend a través de la petición y el endpoint.
  login(data: {email: string, password: string}) {
    return this.http.post<{message: string}>(`${this.url}/login`, data);
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
