import { Component } from '@angular/core';
import { EventService } from '../../services/eventService';
import { Events } from '../../models';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'event-filters',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './event-filters.html',
  styleUrls: ['./event-filters.scss']
})
export class EventFiltersComponent {
  name: string = '';
  startDate: string = '';
  category: string = '';

  events: Events[] = [];
  error: string = '';

  constructor(private eventService: EventService) {}

  searchByName() {
    if (!this.name) return;
    this.eventService.getEventByName(this.name).subscribe({
      next: (event) => {
        this.events = event ? [event] : [];
        this.error = this.events.length ? '' : 'No se encontró ningún evento con ese nombre.';
      },
      error: () => {
        this.events = [];
        this.error = 'No se encontró ningún evento con ese nombre.';
      }
    });
  }

  searchByStartDate() {
    if (!this.startDate) return;
    this.eventService.getEventsByStartDate(this.startDate).subscribe({
      next: (events) => {
        this.events = events;
        this.error = events.length ? '' : 'No hay eventos para esa fecha.';
      },
      error: () => {
        this.events = [];
        this.error = 'Error al buscar por fecha.';
      }
    });
  }

  searchByCategory() {
    if (!this.category) return;
    this.eventService.getEventsByCategory(this.category).subscribe({
      next: (events) => {
        this.events = events;
        this.error = events.length ? '' : 'No hay eventos para esa categoría.';
      },
      error: () => {
        this.events = [];
        this.error = 'Error al buscar por categoría.';
      }
    });
  }

  clearResults() {
    this.events = [];
    this.error = '';
  }
}
