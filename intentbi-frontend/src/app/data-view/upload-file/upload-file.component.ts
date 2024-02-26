import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Message, MessageService } from 'primeng/api';
import { FileBeforeUploadEvent, FileUploadErrorEvent, FileUploadModule, UploadEvent } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { Constant } from '../../configuration/constant';
import { HttpHeaders, HttpStatusCode } from '@angular/common/http';
import { Router } from '@angular/router';
import { ScrollerModule } from 'primeng/scroller';
import { Messages, MessagesModule } from 'primeng/messages';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-upload-file',
  standalone: true,
  imports: [
    FileUploadModule,
    NgIf,
    CommonModule,
    ToastModule,
    ScrollerModule,
    MessagesModule,
    CardModule
  ],
  templateUrl: './upload-file.component.html',
  styleUrl: './upload-file.component.css',
  providers: [MessageService]
})
export class UploadFileComponent implements OnInit {
  uploadedFiles: any[] = [];
  url = `${Constant.URL_PREFIX}/data/xlsx`;
  token?:string;
  headers?: HttpHeaders;
  problems: Message[] = [];
  problemHeader!: string;

  constructor(private messageService: MessageService, private router: Router) {}

  ngOnInit(): void {
    this.token = localStorage.getItem(Constant.AUTH_TOKEN_PROPERTY) || "";
    this.headers = new HttpHeaders();
    this.headers = this.headers.set("Authorization", `Bearer ${this.token}`);
  }

  onUpload(event:UploadEvent) {
    this.messageService.add({severity: 'success', summary: 'File Uploaded', detail: ''});
  }

  private handleBadRequest(error: any) {
    let detail = "";

    if (error.type == "invalid-xlsx-cell-type") {
      let invalids = error.invalid_cells as any[];
      let errs = invalids.map(iv => `Row: ${iv.row_index}, Col: ${iv.col_index} :: Expected ${iv.expected_type}`);

      detail = 'Given Data Contains Invalid Cell Type';
      this.problemHeader = "Invalid Type"
      this.problems = errs.map(e => {return { severity: 'error', detail: e };})
    } else if (error.type == "xlsx-constraint-violation") {
      let invalids = error.constraint_violations as any[];
      let errs:string[] = [];

      for (let iv of invalids) {
        let row = iv.row_no;
        let messages = iv.messages;
        let _errs = Object.keys({...messages}).map(k => `Row: ${row}, Col: ${k} :: ${messages[k]}`);

        errs = [...errs, ..._errs];
      }

      detail = 'Given Data Violates Constraints';
      this.problemHeader = "Constraint Violations"
      this.problems = errs.map(e => {return { severity: 'error', detail: e };})
    } else if (error.type == "invalid-xlsx-type") {
      detail = 'Only XLSX Files are Accepted';
    } else {
      detail = 'Something Went Wrong';
    }

    this.messageService.add({severity: 'error', detail});
  }

  onError(error: FileUploadErrorEvent) {
    console.error(error);
    let detail = "";

    if (error.error?.error.status == 0) {
      detail = "Server Unreachable";
    } else if (error.error?.error.status == HttpStatusCode.Forbidden) {
      this.router.navigate(["/logout"]);
    } else if (error.error?.error.status == HttpStatusCode.BadRequest) {
      this.handleBadRequest(error.error?.error);
      return;
    }  else {
      detail = 'Something Went Wrong';
    }

    this.messageService.add({severity: 'error', detail});
  }

  onBeforeUpload(fileBeforeUploadEvent: FileBeforeUploadEvent) {
    this.problems = [];
  }
}
