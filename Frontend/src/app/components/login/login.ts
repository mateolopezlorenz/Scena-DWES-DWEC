import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
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
      next: (res: any) => {

        //Damos feedback al usuario de que el login se ha completado de forma exitosa.
        alert(res.message);
      },
      error: (err: any) => {

        //Damos feedback al usuario de que el login no se ha completado.
        alert('Error en el login: ' + err.error.message);
        console.error('Error en el login:', err);
      }
    });

  }
}