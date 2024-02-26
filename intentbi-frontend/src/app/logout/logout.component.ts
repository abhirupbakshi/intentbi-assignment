import { Component, OnInit } from '@angular/core';
import { Constant } from '../configuration/constant';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.css'
})
export class LogoutComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit(): void {
    localStorage.removeItem(Constant.AUTH_TOKEN_PROPERTY);
    this.router.navigate([""]);
  }
}
