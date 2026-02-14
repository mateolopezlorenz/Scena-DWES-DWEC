import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { Authservice } from '../../services/authservice';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss'],
})
export class Navbar {

  username: string | null = null;

  constructor(private router: Router, private authService: Authservice) {}

  ngOnInit() {
    this.username = localStorage.getItem('username');
  }

  logout() {
  this.authService.logout();
  this.router.navigate(['/login']);
}
}
