import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Authservice } from '../../services/authservice';

@Component({
  selector: 'home',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss'],
})
export class Home {

  username: string | null = '';

  constructor(private authservice: Authservice, private router: Router) {}

    ngOnInit() {
      this.username = localStorage.getItem('username');
    }

    logout() {
      this.authservice.logout();
      this.router.navigate(['/login'])
    }
}
