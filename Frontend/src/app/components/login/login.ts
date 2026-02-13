import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
})
export class Login {

  //Datos que introduce el usuario en el formulario de login.
  loginData = {
    username: '',
    password: ''
  }

  constructor(private authService: Authservice, private router: Router) {}

  onSubmit() {

    if (!this.loginData.username || !this.loginData.password) {
      alert('Todos los datos son obligatorios!');
      return;
    }


    this.authService.login(this.loginData).subscribe({
      next: (res: any) => {

        this.authService.saveSession(res);

        this.router.navigate(["/event-list"])
      },
      error: (err: any) => {

        //Damos feedback al usuario de que el login no se ha completado.
        alert('Error en el login: ' + err.error.message);
        console.error('Error en el login:', err);
      }
    });

  }
}