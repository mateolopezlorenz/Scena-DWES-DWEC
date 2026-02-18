import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { EventService } from '../../services/eventService';
import { MapView } from '../map-view/map-view';

@Component({
  selector: 'app-event-form',
  standalone: true,
  imports: [FormsModule, CommonModule, MapView],
  templateUrl: './event-form.html',
  styleUrls: ['./event-form.scss'],
})
export class EventForm {
  
  showMapSelector: boolean = false;

  //Datos del evento que se enviarán al backend
  eventData = {
    name: '',
    description: '',
    category: '',
    startDate: '',
    endDate: '',
    latitude: null as number | null,
    longitude: null as number | null,
    address: ''
  };

  constructor(private eventService: EventService, private router: Router) {}

  //Método que envía los datos registrados para poder crear el evento.
  onSubmit() {
    this.eventService.createEvent(this.eventData).subscribe({
      next: (res: any) => {
        alert('Evento creado correctamente: ' + res.name);
        this.router.navigate(['/event-list']);
      },
      error: (err: any) => {
        alert('Error al crear el evento: ' + err.message);
        console.error('Error al crear el evento:', err);
      }
    });
  }

  //Método para capturar coordenadas del mapa
  onCoordinatesSelected(coordinates: { latitude: number, longitude: number, address: string }) {
    this.eventData.latitude = coordinates.latitude;
    this.eventData.longitude = coordinates.longitude;
    this.eventData.address = coordinates.address;
    this.showMapSelector = false;
  }
}
