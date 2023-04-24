import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpServices } from '../app.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { getMessaging, onMessage } from "firebase/messaging";
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css', './mdb.min.css'],
  providers: [ MessageService ]
})
export class NavbarComponent implements OnInit, OnDestroy {

  favWeather!: string
  service$!: Subscription
  userEmail!: string
  firstName!: string

  constructor (private http: HttpServices, private router: Router, private messageService: MessageService) { }

  ngOnInit(): void {
    this.userEmail = JSON.parse(sessionStorage.getItem('user')!)['email']
    this.firstName = JSON.parse(sessionStorage.getItem('user')!)['firstName']
    this.http.getFavWeather(this.userEmail)
      .then(results => {
        console.info(results)
        this.favWeather = results['favWeather']
      })
    
    this.listen()
    this.service$ = this.http.refresh.subscribe(
      (refresh: boolean) => {
        this.ngOnDestroy()
        this.ngOnInit()
      }
    )
  }

  listen() {
    const messaging = getMessaging();
    onMessage(messaging, (payload) => {
      console.log('Message received. ', payload);
      this.messageService.add({ severity: 'success', summary: 'New traffic image received', detail: 'Please refresh the browser', sticky: true });
    });
  }

  logout() {
    sessionStorage.removeItem('user')
    this.router.navigate(['/'])
  }

  ngOnDestroy(): void {
    this.service$.unsubscribe()
  }

}
