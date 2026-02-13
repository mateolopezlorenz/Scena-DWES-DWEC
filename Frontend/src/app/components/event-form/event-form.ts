import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-event-form',
  standalone: true,
  imports: [FormsModule],
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
    capacity: 0,
    rooms: 0
  };
  constructor(private authService: Authservice) {}

  onSubmit() {
    this.authService.eventForm(this.eventData).subscribe({
    next: (res: any) => {
      alert(res.message);
    },
    error: (err: any) => {
      alert('Error al crear el evento: ' + err.message);
      console.error('Error al crear el evento:', err);
    }
  });   
  }
}
