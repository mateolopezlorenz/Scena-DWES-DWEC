import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserEventService {

  private url = 'http://localhost:8080/api/user-events';

  constructor(private http: HttpClient) {}

  // Dar o quitar like en un evento
  toggleLike(eventId: number): Observable<any> {
    return this.http.post<any>(`${this.url}/like?eventId=${eventId}`, {});
  }

  // Obtener relación usuario-evento
  getUserEvent(eventId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/relation?eventId=${eventId}`);
  }

  // Contar likes de un evento
  countLikes(eventId: number): Observable<{likes: number}> {
    return this.http.get<{likes: number}>(`${this.url}/likes/${eventId}`);
  }

  // Obtener eventos que le gustan al usuario
  getLikedByUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/liked`);
  }
}
