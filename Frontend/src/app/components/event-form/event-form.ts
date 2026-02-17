import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LocalService } from '../../services/localService';
import { EventService } from '../../services/eventService';
import { Local } from '../../models';

@Component({
  selector: 'app-event-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './event-form.html',
  styleUrls: ['./event-form.scss'],
})
export class EventForm {
  
  //Lista de locales para poder agregar al evento en el formulario.
  locals: Local[] = [];

  //Datos del evento que se enviarán al backend
  eventData = {
    name: '',
    description: '',
    category: '',
    startDate: '',
    endDate: '',
    capacity: 1,
    rooms: 1,
    localId: null
  };

  constructor(private localService: LocalService, private eventService: EventService) {}

  //Método que se ejecuta y carga la lista de locales disponibles
  ngOnInit() {
    this.localService.getLocals().subscribe({
      next: (res: any) => {
        this.locals = res;
      },
      error: (err: any) => {
        console.error('Error al obtener los locales:', err);
      }
    });
  }

  //Método que envía los datos registrados para poder crear el evento.
  onSubmit() {
    this.eventService.createEvent(this.eventData).subscribe({
    next: (res: any) => {
      alert('Evento creado correctamente: ' + res.name);
    },
    error: (err: any) => {
      alert('Error al crear el evento: ' + err.message);
      console.error('Error al crear el evento:', err);
    }
  });   
  }
}
