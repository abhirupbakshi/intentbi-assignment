import { Component, OnDestroy, OnInit } from '@angular/core';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Data, dataFieldToJsonField, dataFromJson } from '../../model/data';
import { HttpClient, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Constant } from '../../configuration/constant';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [TableModule, ButtonModule, ToastModule, DatePipe],
  providers: [MessageService],
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.css'
})
export class DataTableComponent implements OnInit, OnDestroy {

  dataList: Data[] = [{}];
  totalRecords!: number;
  loading: boolean = false;
  subscription?: Subscription;
  private token!: string;

  constructor(private httpClient: HttpClient, private router: Router, private messageService: MessageService) { }

  ngOnInit() {
    this.token = localStorage.getItem(Constant.AUTH_TOKEN_PROPERTY) || "";
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  loadData(event: TableLazyLoadEvent) {
    let pageNo = Math.floor((event.first as number) / (event.rows as number));
    let pageSize = event.rows as number;
    let sortByJsonField = dataFieldToJsonField(event.sortField as string);
    let direction = event.sortOrder as number > 0 ? "asc" : "desc";

    let url = `${Constant.URL_PREFIX}/data`;
    let options = {
      params: {
        _page: pageNo + 1,
        _size: pageSize,
        _sort: `${sortByJsonField}:${direction}`
      },
      headers: {
        "Authorization": `Bearer ${this.token}`
      }
    }

    let next = (body: any) => {
      this.totalRecords = body["X-Total-Count"];
      this.dataList = body.data.map((d: any) => dataFromJson(d));
      this.loading = false;
    };

    let error = (error: any) => {
      console.error(error);

      let detail = "";
      if (error.status == 0) {
        detail = "Cannot Reach Server";
      } else if (error.status == HttpStatusCode.Forbidden) {
        this.router.navigate(["/logout"]);
      } else {
        detail = "Someting Went Wrong";
      }

      this.messageService.add({ severity: 'error', detail });
      this.loading = false;
    };

    let observable = this.httpClient.get<any>(url, options);
    this.subscription = observable.subscribe({ next, error });
    this.loading = true;
  }

  editData(data: Data) {
    let params: any = { ...data, _edit: true };

    params.date = data.date ? data.date.toISOString().split('T')[0] : null;
    this.router.navigate(["/data/save"], { queryParams: params })
  }

  deleteData(data: Data) {
    console.log(data.id);
    let url = `${Constant.URL_PREFIX}/data/${data.id}`;
    let options = {
      headers: {
        "Authorization": `Bearer ${this.token}`
      }
    }

    let next = (body: any) => {
      window.location.reload();
    };

    let error = (error: any) => {
      console.error(error);

      let detail = "";
      if (error.status == 0) {
        detail = "Server Unreachable";
      } else if (error.status == HttpStatusCode.Forbidden) {
        this.router.navigate(["/logout"]);
      } else if (error.status == HttpStatusCode.NotFound) {
        detail = "Data Not Found On Server";
      } else {
        detail = "Someting Went Wrong";
      }

      this.messageService.add({ severity: 'error', detail });
    };

    let observable = this.httpClient.delete<any>(url, options);
    observable.subscribe({ next, error });
  }
}
