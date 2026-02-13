import { Component } from '@angular/core';
import { Authservice } from '../../services/authservice';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { RegisterRequest } from '../../models';
import { Router } from '@angular/router'

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterModule],
  standalone: true,
  templateUrl: './register.html',
  styleUrls: ['./register.scss'],
})
export class Register {

  //Datos del registro.
  registerData: RegisterRequest = {
    username: '',
    email: '',
    password: '',
  }


  constructor(private authService: Authservice, private router: Router) {}

  //Método que envía los datos del registro al servicio de autenticación.
  onSubmit() {
    this.authService.register(this.registerData).subscribe({
      next: () => {
        //Redirigimos al login.
        this.router.navigate(["/login"]);
      },
      error: (err) => {
        
        //Mostramos un mensaje de error al usuario cuando no se ha completado el registro.
        alert('Error en el registro: ' + err.error.message);
        console.error('Error en el registro:', err);
      }
    })

  }
}
