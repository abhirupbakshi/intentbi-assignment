import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { Constant } from '../configuration/constant';

@Component({
  selector: 'app-data-view',
  standalone: true,
  imports: [MenubarModule, RouterOutlet],
  templateUrl: './data-view.component.html',
  styleUrl: './data-view.component.css'
})
export class DataViewComponent implements OnInit, OnDestroy {
  items: MenuItem[] = [
    { label: 'DataTable', routerLink: "/data/table" },
    { label: 'Upload XLSX', routerLink: "/data/upload" },
    { label: 'Save Data', routerLink: "/data/save" },
    { label: 'Log out', routerLink: "/logout" }
  ];

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    if (localStorage.getItem(Constant.AUTH_TOKEN_PROPERTY) == null) {
      this.router.navigate([""]);
    }

    this.router.navigate(["/data/table"]);
  }

  ngOnDestroy(): void {
  }
}
