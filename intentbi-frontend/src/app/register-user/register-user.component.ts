import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { MessagesModule } from 'primeng/messages';
import { ToastModule } from 'primeng/toast';
import { Subscription } from 'rxjs';
import { Constant } from '../configuration/constant';
import { GLOBAL } from '../global.data';

@Component({
  selector: 'app-register-user',
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
  templateUrl: './register-user.component.html',
  styleUrl: './register-user.component.css'
})
export class RegisterUserComponent implements OnInit, OnDestroy {

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

  register() {
    let username = this.formGroup.value.username;
    let password = this.formGroup.value.password;
    let url = `${Constant.URL_PREFIX}/user`;
    let body = { username, password };
    let options = {
      observe: 'response' as const
    };

    let next = (body: any) => {
      this.router.navigate([""]);
    }

    let error = (err: any) => {
      console.error('Error:', err);

      let detail = "";

      if (err.status == 0) {
        detail = "Server Unreachable";
      } else if (err.error.type == "user-exists") {
        detail = "User Already Exists";
      } else {
        detail = "Something Went Wrong";
      }

      this.messageService.add({ severity: 'error', detail });
      this.loading = false;
    }

    if (!username || !password) {
      return;
    }

    let observable = this.httpClient.post<any>(url, body, options);
    this.subscription = observable.subscribe({ next, error });

    this.loading = true;
  }
}
