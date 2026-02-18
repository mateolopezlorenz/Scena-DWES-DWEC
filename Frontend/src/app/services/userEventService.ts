import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserEventService {

  private eventsUrl = 'http://localhost:8080/api/events';
  private usersUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  // Añadir evento a favoritos
  addLike(eventId: number): Observable<any> {
    return this.http.post<any>(`${this.eventsUrl}/${eventId}/like`, {});
  }

  // Eliminar evento de favoritos
  removeLike(eventId: number): Observable<any> {
    return this.http.delete<any>(`${this.eventsUrl}/${eventId}/like`);
  }

  // Contar likes de un evento (público)
  countLikes(eventId: number): Observable<{likes: number}> {
    return this.http.get<{likes: number}>(`${this.eventsUrl}/${eventId}/likes/count`);
  }

  // Obtener eventos favoritos del usuario autenticado
  getLikedByUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.usersUrl}/me/likes`);
  }
}
