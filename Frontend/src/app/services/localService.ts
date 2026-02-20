import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Local } from '../models';

@Injectable({
  providedIn: 'root',
})
export class LocalService {

  private url = 'http://localhost:8080/api/locals';

  constructor(private http: HttpClient) {}

  crearLocal(data: any): Observable<Local> {
    return this.http.post<Local>(this.url, data);
  }

  getLocals(): Observable<Local[]> {
    return this.http.get<Local[]>(this.url);
  }

  getLocalsByUser(): Observable<Local[]> {
    return this.http.get<Local[]>(`${this.url}/user`);
  }

  getLocalById(id: number): Observable<Local> {
    return this.http.get<Local>(`${this.url}/${id}`);
  }

  updateLocal(id: number, data: any): Observable<Local> {
    return this.http.put<Local>(`${this.url}/${id}`, data);
  }

  deleteLocal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
