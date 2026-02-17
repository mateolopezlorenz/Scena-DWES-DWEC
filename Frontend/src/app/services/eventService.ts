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

  //Método que envía los datos registrados al backend para crear el evento.
  createEvent(data: any): Observable<Events> {

    //Obtenemos el token de localStorage.
    const token = localStorage.getItem('token');
    
    //Construímos los headers de la petición para añadir el token de autenticación.
    const headers = {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` })
    };

    //Devolvemos la petición al backend para crear el evento.
    return this.http.post<Events>(`${this.url}/createEvent`, data, { headers });
  }

  //Método para obtener todos los eventos.
  getAllEvents(): Observable<Events[]> {
    return this.http.get<Events[]>(`${this.url}/all`);
  }

  //Método para obtener un evento por id.
  getEventById(id: number): Observable<Events> {
    return this.http.get<Events>(`${this.url}/${id}`);
  }

  //Método para obtener un evento por nombre.
  getEventByName(name: string): Observable<Events> {
    return this.http.get<Events>(`${this.url}/searchEvent/${name}`);
  }

  //Método para obtener eventos por fecha de inicio.
  getEventsByStartDate(startDate: string): Observable<Events[]> {
    return this.http.get<Events[]>(`${this.url}/startDate/${startDate}`);
  }

  //Método para obtener eventos por categoría.
  getEventsByCategory(category: string): Observable<Events[]> {
    return this.http.get<Events[]>(`${this.url}/category/${category}`);
  }
}
