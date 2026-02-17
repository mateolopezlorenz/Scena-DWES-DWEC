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
  crearLocal(data: Local):Observable<Local> {
    const token = localStorage.getItem('token');
    console.log('LOCAL-SERVICE: POST a', `${this.url}/createLocal`);
    console.log('Token disponible:', token ? 'SÍ' : 'NO');
    
    //Construímos los headers de la petición.
    const headers = {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` })
    };
    
    console.log('Headers que se envían:', Object.keys(headers));
    if (token) {
      console.log('Authorization header:', headers['Authorization']);
    }
    
    return this.http.post<Local>(`${this.url}/createLocal`, data, { headers });
  }

  getLocals(): Observable<Local[]> {
    const token = localStorage.getItem('token');
    const headers = {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` })
    };
    
    return this.http.get<Local[]>(`${this.url}/user`, { headers });
  }

}
