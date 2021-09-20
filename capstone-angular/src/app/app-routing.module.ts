import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/login/login.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import { NavigationComponent } from './components/navigation/navigation.component';
import { MatdashboardComponent } from './components/matdashboard/matdashboard.component';
import {HistoryComponent} from "./components/history/history.component";

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'navigation', component: NavigationComponent},
  {path: 'history', component: HistoryComponent},
  {path: 'matdashboard', component: MatdashboardComponent},
  {path: '**', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
