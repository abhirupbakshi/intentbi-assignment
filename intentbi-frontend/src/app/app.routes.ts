import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterUserComponent } from './register-user/register-user.component';
import { AppComponent } from './app.component';
import { DataViewComponent } from './data-view/data-view.component';
import { DataTableComponent } from './data-view/data-table/data-table.component';
import { SaveDataComponent } from './data-view/save-data/save-data.component';
import { UploadFileComponent } from './data-view/upload-file/upload-file.component';
import { LogoutComponent } from './logout/logout.component';

export const routes: Routes = [
  { path: "", component: AppComponent },
  { path: "login", component: LoginComponent },
  { path: "logout", component: LogoutComponent },
  { path: "register", component: RegisterUserComponent },
  {
    path: "data", component: DataViewComponent, children: [
      { path: "table", component: DataTableComponent },
      { path: "save", component: SaveDataComponent },
      { path: "upload", component: UploadFileComponent }
    ]
  }
];
