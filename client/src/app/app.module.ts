import { NgModule, Type, inject } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AgmCoreModule } from '@agm/core';

import { AppComponent } from './app.component';
import { LoginComponent } from './loginregister/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { MainComponent } from './canvas/main.component';
import { CanActivateFn, RouterModule, Routes } from '@angular/router';
import { BusComponent } from './homepages/bus.component'
import { HttpServices } from './app.service';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { WeatherComponent } from './homepages/weather.component';
import { TrainComponent } from './homepages/train.component';
import { RegisterComponent } from './loginregister/register.component';
import { TrafficComponent } from './homepages/traffic.component';
import { TrafficUpdatesComponent } from './homepages/trafficupdates.component'
import { UserRepository } from './user.repository';
import { exitAboutGuard } from './exit.about.guard';
import { ButtonModule } from 'primeng/button';
import { environment } from "../environments/environment";
import { initializeApp } from "firebase/app";
import { CameraComponent } from './homepages/camera.component';
import { WebcamModule } from 'ngx-webcam'
import { DropdownModule } from 'primeng/dropdown';
import {FormsModule} from '@angular/forms';
import { DonateComponent } from './homepages/donate.component';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { CardModule } from 'primeng/card';
import { ThankYouComponent } from './homepages/thank-you.component';
import { ToastModule } from 'primeng/toast';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

initializeApp(environment.firebase);

function mapToActivate(providers: Array<Type<{canActivate: CanActivateFn}>>): CanActivateFn[] {
  return providers.map(provider => (...params) => inject(provider).canActivate(...params));
}

const appRoutes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'register', component: RegisterComponent},
  { path: 'home', component: MainComponent,  canActivate: mapToActivate([UserRepository]),
    children: [
      { path: '', component: BusComponent },  // use canDeactivate: [UserRepository] / canDeactivate: [exitAboutGuard]
      { path: 'bus', component: BusComponent },
      { path: 'weather', component: WeatherComponent },
      { path: 'train', component: TrainComponent },
      { path: 'trafficImages', component: TrafficComponent },
      { path: 'trafficUpdates', component: TrafficUpdatesComponent },
      { path: 'camera', component: CameraComponent },
      { path: 'donate', component: DonateComponent },
      { path: 'thankyou', component: ThankYouComponent }
  ]},
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavbarComponent,
    MainComponent,
    BusComponent,
    WeatherComponent,
    TrainComponent,
    RegisterComponent,
    TrafficComponent,
    TrafficUpdatesComponent,
    CameraComponent,
    DonateComponent,
    ThankYouComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes, { useHash: true }),
    ReactiveFormsModule,
    HttpClientModule,
    AgmCoreModule.forRoot({
      apiKey: environment.apiKey
    }),
    ButtonModule,
    WebcamModule,
    DropdownModule,
    FormsModule,
    ScrollPanelModule,
    CardModule,
    ToastModule,
    BrowserAnimationsModule
  ],
  providers: [ HttpServices ],
  bootstrap: [AppComponent]
})
export class AppModule { }
