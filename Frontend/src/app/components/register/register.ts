import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';

@Component({
  selector: 'app-register',
  imports: [],
  standalone: true,
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {

  //Datos del registro.
  registerData = {
    username: '',
    email: '',
    password: '',
  }


  constructor(private authService: Authservice) {}

  //Método que envía los datos del registro al servicio de autenticación.
  onSubmit() {
    this.authService.register(this.registerData).subscribe({
      next: (res) => {

        //Mostramos un mensaje al usuario de que el registro se ha completado.
        alert(res.message);

        //Limpiamos los datos del formulario cuando el registro se ha completado.
        this.registerData = {username: '', email: '', password: ''}
      },
      error: (err) => {
        
        //Mostramos un mensaje de error al usuario cuando no se ha completado el registro.
        alert('Error en el registro: ' + err.error.message);
        console.error('Error en el registro:', err);
      }
    })

  }
}
