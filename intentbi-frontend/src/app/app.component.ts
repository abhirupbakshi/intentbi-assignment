import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { LoginComponent } from './login/login.component';
import { Constant } from './configuration/constant';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ButtonModule, LoginComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'intentbi-frontend';

  constructor(private router: Router) { }

  ngOnInit(): void {
    if (localStorage.getItem(Constant.AUTH_TOKEN_PROPERTY) == null) {
      this.router.navigate(["login"]);
    } else {
      this.router.navigate(["data"]);
    }
  }
}
