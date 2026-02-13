import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {

  //Datos que introduce el usuario en el formulario de login.
  loginData = {
    username: '',
    password: ''
  }

  constructor(private authService: Authservice) {}

  onSubmit() {
    this.authService.login(this.loginData).subscribe({
      next: (res) => {

        //Damos feedback al usuario de que el login se ha completado de forma exitosa.
        alert(res.message);
      },
      error: (err) => {

        //Damos feedback al usuario de que el login no se ha completado.
        alert('Error en el login: ' + err.error.message);
        console.error('Error en el login:', err);
      }
    });

  }
}