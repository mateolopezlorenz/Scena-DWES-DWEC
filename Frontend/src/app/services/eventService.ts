import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Events } from '../models';

@Injectable({
  providedIn: 'root',
})
export class EventService {

  private url = 'http://localhost:8080/api/events';

  constructor(private http: HttpClient) {}

  getAllEvents(): Observable<Events[]> {
    return this.http.get<Events[]>(this.url);
  }

  getEventById(id: number): Observable<Events> {
    return this.http.get<Events>(`${this.url}/${id}`);
  }
}
