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

  createEvent(data: any): Observable<Events> {
    const token = localStorage.getItem('token');
    
    //Construímos los headers de la petición.
    const headers = {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` })
    };
    return this.http.post<Events>(`${this.url}/createEvent`, data, { headers });
  }

  getAllEvents(): Observable<Events[]> {
    return this.http.get<Events[]>(`${this.url}/all`);
  }

  getEventById(id: number): Observable<Events> {
    return this.http.get<Events>(`${this.url}/${id}`);
  }

  getEventByName(name: string): Observable<Events> {
    return this.http.get<Events>(`${this.url}/searchEvent/${name}`);
  }

 
  getEventsByStartDate(startDate: string): Observable<Events[]> {
    return this.http.get<Events[]>(`${this.url}/startDate/${startDate}`);
  }

  getEventsByCategory(category: string): Observable<Events[]> {
    return this.http.get<Events[]>(`${this.url}/category/${category}`);
  }
}
