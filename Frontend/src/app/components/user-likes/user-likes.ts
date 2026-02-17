import { Component, OnInit } from '@angular/core';
import { UserEventService } from '../../services/userEventService';
import { Events } from '../../models/eventModel';

@Component({
  selector: 'user-likes',
  templateUrl: './user-likes.html',
})
export class UserLikesComponent implements OnInit {
  likedEvents: Events[] = [];
  error: string = '';

  constructor(private userEventService: UserEventService) {}
  ngOnInit() {
    this.getLikedEvents();
  }

  getLikedEvents() {
    this.userEventService.getLikedByUser().subscribe({
      next: (events) => {
        this.likedEvents = events;
        this.error = events.length ? '' : 'No tienes eventos con MG.';
      },
      error: () => {
        this.likedEvents = [];
        this.error = 'Error al cargar tus MG.';
      }
    });
  }
}
