import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Constant } from '../configuration/constant';
import { Message, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { MessagesModule } from 'primeng/messages';
import { Subscription } from 'rxjs';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { GLOBAL } from '../global.data';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ButtonModule,
    InputTextModule,
    FormsModule,
    CardModule,
    ReactiveFormsModule,
    ToastModule,
    MessagesModule,
    RouterOutlet,
    RouterLink
  ],
  providers: [MessageService],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit, OnDestroy {

  loading: boolean = false;
  formGroup!: FormGroup;
  subscription?: Subscription;

  constructor(private httpClient: HttpClient, private messageService: MessageService, private router: Router) {
  }

  ngOnInit() {
    if (localStorage.getItem(Constant.AUTH_TOKEN_PROPERTY) != null) {
      this.router.navigate([""]);
    }

    this.formGroup = new FormGroup({
      username: new FormControl<string | null>(null),
      password: new FormControl<string | null>(null)
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  login() {
    let username = this.formGroup.value.username;
    let password = this.formGroup.value.password;
    let url = `${Constant.URL_PREFIX}/auth/login`;
    let options = {
      headers: {
        "Authorization": `Basic ${btoa(username + ":" + password)}`
      }
    };

    let next = (body: any) => {
      let p = Constant.AUTH_TOKEN_PROPERTY;
      GLOBAL[p] = body[p];
      localStorage.setItem(p, body[p]);

      this.router.navigate([""]);
    }

    let error = (err: any) => {
      console.error('Error:', err);

      let detail = "";
      switch (err.status) {
        case 0:
          detail = "Server Unreachable";
          break;
        case HttpStatusCode.Forbidden:
          detail = "Wrong Credentials"
          break;
        default:
          detail = "Something Went Wrong"
      }

      this.messageService.add({ severity: 'error', detail });
      this.loading = false;
    }

    if (!username || !password) {
      return;
    }

    let observable = this.httpClient.post<any>(url, null, options);
    this.subscription = observable.subscribe({ next, error });

    this.loading = true;
  }
}
