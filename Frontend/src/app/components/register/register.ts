import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-register',
  imports: [FormsModule, HttpClientModule],
  standalone: true,
  templateUrl: './register.html',
  styleUrls: ['./register.scss'],
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
      next: (res : any) => {

        //Mostramos un mensaje al usuario de que el registro se ha completado.
        alert(res.message);

        //Limpiamos los datos del formulario cuando el registro se ha completado.
        this.registerData = {username: '', email: '', password: ''}
      },
      error: (err : any) => {
        
        //Mostramos un mensaje de error al usuario cuando no se ha completado el registro.
        alert('Error en el registro: ' + err.error.message);
        console.error('Error en el registro:', err);
      }
    })

  }
}
