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

  register(data: {username: string, email: string, password: string}) {
    return this.http.post<{message: string}>(`${this.url}/register`, data);
  }

  login(data: {email: string, password: string}) {
    return this.http.post<{message: string}>(`${this.url}/login`, data);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return this.isLoggedIn;
  }

  logout(): void {
    localStorage.clear();
    this.isLoggedIn = false;
  }

  saveSession(response: LoginResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('username', response.username);
    localStorage.setItem('email', response.email);
    localStorage.setItem('id', response.id.toString());

    this.isLoggedIn = true;
  }
}
