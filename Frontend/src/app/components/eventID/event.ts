import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { EventService } from '../../services/eventService';
import { Events } from '../../models/eventModel';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-event',
  imports: [RouterModule, CommonModule],
  templateUrl: './event.html',
  styleUrl: './event.scss',
})
export class Event implements OnInit {
  event: Events | null = null;
  eventId: number | null = null;

  constructor(private route: ActivatedRoute, private eventService: EventService ) {}

  //Obtenemos el ID del evento y cargamos sus datos.
  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.eventId = params['id'];
      if (this.eventId) {
        this.loadEvent();
      }
    });
  }

  //Método que carga los datos del evento a través del servicio.
  loadEvent(): void {
    if (this.eventId) {
      this.eventService.getEventById(this.eventId).subscribe({
        next: (data: Events) => this.event = data,
        error: (err) => console.error('Error al cargar el evento', err)
      });
    }
  }
}
