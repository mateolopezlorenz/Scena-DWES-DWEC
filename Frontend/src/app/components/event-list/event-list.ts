import { Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { EventService } from '../../services/eventService';
import { Events } from '../../models/event.model';

@Component({
  selector: 'event-list',
  imports: [RouterModule],
  templateUrl: './event-list.html',
  styleUrls: ['./event-list.scss'],
})
export class EventList implements OnInit {

  //Lista de eventos.
  events: Events[] = [];

  constructor(private eventService: EventService, private router: Router) {}
  
  //Se carga la lista de eventos al acceder a la página.
  ngOnInit(){
    this.loadEvents();
  }

  //Método para cargar los eventos.
  loadEvents() {
  this.eventService.getAllEvents().subscribe({
    next: (data: Events[]) => this.events = data,
    error: (err) => console.error('Error al cargar los eventos', err)
  });
}


  //Método para ver los detalles del evento.
  viewEvent(id: number) {
    this.router.navigate(['/event', id]);
  }

}
