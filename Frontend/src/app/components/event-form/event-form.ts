import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-event-form',
  imports: [],
  templateUrl: './event-form.html',
  styleUrl: './event-form.scss',
})
export class EventForm {
 
  eventData = {
    name: '',
    description: '',
    category: '',
    startDate: '',
    endDate: '',
    capacity: '',
    rooms: ''
  };
  constructor(private authService: AuthService) {}

  onSubmit() {
    this.authService.eventForm(this.eventData).subscribe({
    next: (res) => {
      alert(res.message);
    },
    error: (err) => {
      alert('Error al crear el evento: ' + err.message);
      console.error('Error al crear el evento:', err);
    }
  });   
  }
}
