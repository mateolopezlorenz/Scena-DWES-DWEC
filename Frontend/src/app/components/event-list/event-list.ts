import { Component, OnInit, Input } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { EventService } from '../../services/eventService';
import { UserEventService } from '../../services/userEventService';
import { Events } from '../../models/eventModel';
import { CommonModule } from '@angular/common';
import { EventFiltersComponent, EventFilters } from '../event-filters/event-filters';
import { MapView } from '../map-view/map-view';

@Component({
  selector: 'event-list',
  imports: [RouterModule, CommonModule, EventFiltersComponent, MapView],
  templateUrl: './event-list.html',
  styleUrls: ['./event-list.scss'],
})
export class EventList implements OnInit {

  @Input() inputEvents: Events[] | null = null;
  @Input() title: string = 'Lista de Eventos';

  events: Events[] = [];
  isLoggedIn = false;
  likeCounts: { [eventId: number]: number } = {};
  likedEventIds: Set<number> = new Set();
  showFilters = true;

  constructor(
    private eventService: EventService,
    private userEventService: UserEventService,
    private router: Router
  ) {}

  ngOnInit() {
    this.isLoggedIn = !!localStorage.getItem('token');
    this.showFilters = this.inputEvents === null;
    if (this.inputEvents !== null) {
      this.events = this.inputEvents;
      this.loadLikeCounts();
      if (this.isLoggedIn) {
        this.loadUserLikes();
      }
    } else {
      this.loadEvents();
    }
  }

  loadEvents() {
    this.eventService.getAllEvents().subscribe({
      next: (data: Events[]) => {
        this.events = this.sortEventsByDate(data);
        this.loadLikeCounts();
        if (this.isLoggedIn) {
          this.loadUserLikes();
        }
      },
      error: (err) => console.error('Error al cargar los eventos', err)
    });
  }

  onFiltersChanged(filters: EventFilters) {
    const hasFilters = filters.category || filters.date || filters.search;
    if (!hasFilters) {
      this.loadEvents();
      return;
    }
    this.eventService.getFilteredEvents(filters).subscribe({
      next: (data: Events[]) => {
        this.events = this.sortEventsByDate(data);
        this.loadLikeCounts();
        if (this.isLoggedIn) {
          this.loadUserLikes();
        }
      },
      error: (err) => console.error('Error al filtrar los eventos', err)
    });
  }

  sortEventsByDate(events: Events[]): Events[] {
    return events.sort((a, b) => {
      const fechaInicio = new Date(a.startDate).getTime();
      const fechaFin = new Date(b.startDate).getTime();
      return fechaInicio - fechaFin;
    });
  }

  loadLikeCounts() {
    for (const event of this.events) {
      this.userEventService.countLikes(event.id).subscribe({
        next: (res) => this.likeCounts[event.id] = res.likes,
        error: () => this.likeCounts[event.id] = 0
      });
    }
  }

  loadUserLikes() {
    this.userEventService.getLikedByUser().subscribe({
      next: (likedEvents: Events[]) => {
        this.likedEventIds = new Set(likedEvents.map(e => e.id));
      },
      error: () => this.likedEventIds = new Set()
    });
  }

  toggleLike(eventId: number) {
    if (this.isLiked(eventId)) {
      this.userEventService.removeLike(eventId).subscribe({
        next: () => {
          this.likedEventIds.delete(eventId);
          this.likeCounts[eventId] = Math.max((this.likeCounts[eventId] || 0) - 1, 0);
        },
        error: (err) => console.error('Error al quitar like', err)
      });
    } else {
      this.userEventService.addLike(eventId).subscribe({
        next: () => {
          this.likedEventIds.add(eventId);
          this.likeCounts[eventId] = (this.likeCounts[eventId] || 0) + 1;
        },
        error: (err) => console.error('Error al dar like', err)
      });
    }
  }

  isLiked(eventId: number): boolean {
    return this.likedEventIds.has(eventId);
  }

  viewEvent(id: number) {
    this.router.navigate(['/event', id]);
  }
}