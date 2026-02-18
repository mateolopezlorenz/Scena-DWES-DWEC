import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';
import { LocalService } from '../../services/localService';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-local-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './local-form.html',
  styleUrl: './local-form.scss',
})
export class LocalForm {

  //Datos del local que se enviarán al backend.
  localData = {
    name: '',
    latitude: 0,
    longitude: 0,
    ubication: '',
    capacity: 1,
    rooms: 1
  };

  constructor(private authService: Authservice, private localService: LocalService ) {}

  onSubmit() {    
    this.localService.crearLocal(this.localData).subscribe({
      next: (res: any) => {
        console.log('Local creado correctamente', res);
        alert('Local creado con éxito');
      },
      error: (err: any) => {
        console.error('Error al crear el local', err);
        alert('Error al crear el local: ' + (err.error?.message || err.message));
      }
    });
  }
}
