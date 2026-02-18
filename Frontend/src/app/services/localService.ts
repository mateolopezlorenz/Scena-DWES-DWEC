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

  //Método que envía los datos registrados al backend para poder crear el local.
  crearLocal(data: any): Observable<Local> {
    return this.http.post<Local>(`${this.url}/createLocal`, data);
  }

  getLocals(): Observable<Local[]> {
    return this.http.get<Local[]>(`${this.url}/all`);
  }

}
