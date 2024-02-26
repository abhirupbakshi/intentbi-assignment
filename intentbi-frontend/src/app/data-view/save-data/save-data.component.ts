import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { MessagesModule } from 'primeng/messages';
import { ToastModule } from 'primeng/toast';
import { Subscription } from 'rxjs';
import { Constant } from '../../configuration/constant';
import { GLOBAL } from '../../global.data';
import { DiscountBand, Month, dataFieldToJsonField } from '../../model/data';
import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-save-data',
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
    RouterLink,
    CalendarModule
  ],
  providers: [MessageService],
  templateUrl: './save-data.component.html',
  styleUrl: './save-data.component.css'
})
export class SaveDataComponent {

  loading: boolean = false;
  formGroup!: FormGroup;
  subscription?: Subscription;
  edit = false;
  formHeaderLabel!: string;
  formSumbitLabel!: string;
  private token!: string;

  constructor(private httpClient: HttpClient, private messageService: MessageService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.token = localStorage.getItem(Constant.AUTH_TOKEN_PROPERTY) || "";

    this.formGroup = new FormGroup({
      id: new FormControl<string | null>(null),
      market: new FormControl<string | null>(null),
      country: new FormControl<string | null>(null),
      product: new FormControl<string | null>(null),
      discountBand: new FormControl<DiscountBand | null>(null),
      discount: new FormControl<number | null>(null),
      unitsSold: new FormControl<number | null>(null),
      manufacturingPrice: new FormControl<number | null>(null),
      salePrice: new FormControl<number | null>(null),
      grossSales: new FormControl<number | null>(null),
      sales: new FormControl<number | null>(null),
      COGS: new FormControl<number | null>(null),
      profit: new FormControl<number | null>(null),
      date: new FormControl<Date | null>(null),
      monthNumber: new FormControl<number | null>(null),
      monthName: new FormControl<Month | null>(null),
      year: new FormControl<number | null>(null),
    });

    this.route.queryParams.subscribe(params => {
      if (params["_edit"]) {
        for (let k of Object.keys(params)) {
          if (k == 'date') {
            this.formGroup.get(k)?.setValue(new Date(params[k]));
            continue;
          }

          this.formGroup.get(k)?.setValue(params[k]);
        }

        this.formHeaderLabel = "Edit Data";
        this.formSumbitLabel = "Update";
        this.edit = true;
      } else {
        this.formHeaderLabel = "Add Data";
        this.formSumbitLabel = "Create";
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  private collectValues() {
    let values: any = {};

    for (let k of Object.keys(this.formGroup.value)) {
      let value: string | null;

      if (k == 'date') {
        let date: any = this.formGroup.value['date'];
        date = date ? date.toISOString().split('T')[0] : null;
        value = date;
      } else {
        value = this.formGroup.value[k];
      }

      values[dataFieldToJsonField(k)] = value;
    }

    return values;
  }

  private jsonToFormattedPropertyName(name: string) {
    return name.split("_").map(n => n.charAt(0).toUpperCase() + n.substring(1)).join(" ");
  }

  private createMsgForBadRequest(error: any) {
    if (error.error.type == 'invalid-property-type') {
      let joined = error.error.invalid_properties.join(", ");
      return `Invalid Field Values Near: ${joined}`;
    } else if (error.error.type == 'data-absent') {
      return `Data Absent on Server`;
    } else if (error.error.type == 'constraint-violation') {
      let violations: string[] = [];
      let unformatted = error.error.constraint_violations;

      for (let k in unformatted) {
        violations.push(`${this.jsonToFormattedPropertyName(k)}: ${unformatted[k]}`);
      }

      return violations;
    }
    else {
      return "Someting Went Wrong";
    }
  }

  private handleError(error: any) {
    let detail: string | string[] = "";

    if (error.status == 0) {
      detail = "Server Unreachable";
    } else if (error.status == HttpStatusCode.Forbidden) {
      this.router.navigate(["/logout"]);
    } else if (error.status == HttpStatusCode.NotFound) {
      detail = "Data Not Found On Server";
    } else if (error.status == HttpStatusCode.BadRequest) {
      detail = this.createMsgForBadRequest(error);
    } else {
      detail = "Someting Went Wrong";
    }

    if (typeof detail != 'string') {
      for (let d of detail as []) {
        this.messageService.add({ severity: 'error', detail: d });
      }
    } else {
      this.messageService.add({ severity: 'error', detail });
    }
  }

  save() {
    let body = this.collectValues()
    let url = `${Constant.URL_PREFIX}/data${this.edit ? '/' + body.id : ''}`;
    let options = {
      headers: {
        "Authorization": `Bearer ${this.token}`
      }
    }

    let next = (body: any) => {
      this.messageService.add({ severity: 'success', detail: `${this.edit ? 'Updated' : 'Created'} Successfully` });
      this.loading = false;
    };

    let error = (error: any) => {
      console.error(error);
      this.handleError(error);
      this.loading = false;
    };

    let observable = this.httpClient.post<any>(url, body, options);
    this.subscription = observable.subscribe({ next, error });

    this.loading = true;
  }
}
