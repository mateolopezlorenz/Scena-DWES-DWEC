import { Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { EventService } from '../../services/eventService';
import { UserEventService } from '../../services/userEventService';
import { Events } from '../../models/eventModel';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'event-list',
  imports: [RouterModule, CommonModule],
  templateUrl: './event-list.html',
  styleUrls: ['./event-list.scss'],
})
export class EventList implements OnInit {

  //Lista de eventos.
  events: Events[] = [];

  //Estado de likes.
  isLoggedIn = false;
  likeCounts: { [eventId: number]: number } = {};
  likedEventIds: Set<number> = new Set();

  constructor(
    private eventService: EventService,
    private userEventService: UserEventService,
    private router: Router
  ) {}

  //Se carga la lista de eventos al acceder a la página.
  ngOnInit() {
    this.isLoggedIn = !!localStorage.getItem('token');
    this.loadEvents();
  }

  //Método para cargar los eventos.
  loadEvents() {
    this.eventService.getAllEvents().subscribe({
      next: (data: Events[]) => {
        this.events = data;
        this.loadLikeCounts();
        if (this.isLoggedIn) {
          this.loadUserLikes();
        }
      },
      error: (err) => console.error('Error al cargar los eventos', err)
    });
  }

  //Cargar conteo de MG para cada evento.
  loadLikeCounts() {
    for (const event of this.events) {
      this.userEventService.countLikes(event.id).subscribe({
        next: (res) => this.likeCounts[event.id] = res.likes,
        error: () => this.likeCounts[event.id] = 0
      });
    }
  }

  //Cargar qué eventos ha dado MG el usuario.
  loadUserLikes() {
    this.userEventService.getLikedByUser().subscribe({
      next: (likedEvents: Events[]) => {
        this.likedEventIds = new Set(likedEvents.map(e => e.id));
      },
      error: () => this.likedEventIds = new Set()
    });
  }

  //Dar o quitar MG a un evento.
  toggleLike(eventId: number) {
    this.userEventService.toggleLike(eventId).subscribe({
      next: (res: any) => {
        if (res.liked) {
          this.likedEventIds.add(eventId);
          this.likeCounts[eventId] = (this.likeCounts[eventId] || 0) + 1;
        } else {
          this.likedEventIds.delete(eventId);
          this.likeCounts[eventId] = Math.max((this.likeCounts[eventId] || 0) - 1, 0);
        }
      },
      error: (err) => console.error('Error al dar MG', err)
    });
  }

  //Comprobar si el usuario ha dado MG a un evento.
  isLiked(eventId: number): boolean {
    return this.likedEventIds.has(eventId);
  }

  //Método para ver los detalles del evento.
  viewEvent(id: number) {
    this.router.navigate(['/event', id]);
  }
}
