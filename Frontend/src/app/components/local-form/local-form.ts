import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-local-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './local-form.html',
  styleUrl: './local-form.scss',
})
export class LocalForm {

  localData = {
    name: '',
    latitude: 0,
    longitude: 0,
    ubication: '',
    capacity: 0,
    rooms: 0
  };

  constructor(private authService: Authservice) {}

  onSubmit() {
    this.authService.localForm(this.localData).subscribe({
      next: (res: any) => {
        alert('Local creado con éxito');
      },
      error: (err: any) => {
        alert('Error al crear el local: ' + (err.error?.message || err.message));
        console.error('Error al crear el local:', err);
      }
    });
  }
}
