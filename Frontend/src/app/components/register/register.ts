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
    password2: ''
  }


  constructor(private authService: Authservice, private router: Router) {}

  //Método que envía los datos del registro al servicio de autenticación.
  onSubmit() {

    //Validamos si las contraseñas coincide, si no es así, mostramos un mensaje de error al usuario.
    if (this.registerData.password !== this.registerData.password2) {
      alert('Las contraseñas no coinciden');
      return;
    }

    this.authService.register(this.registerData).subscribe({
      next: () => {
        //Redirigimos al login.
        alert('Registro hecho.')
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
