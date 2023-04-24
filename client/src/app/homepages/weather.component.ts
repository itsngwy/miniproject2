import { Component, OnInit } from '@angular/core';
import { HttpServices } from '../app.service';
import { Weathers } from '../models';
import { Subscription } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css']
})
export class WeatherComponent implements OnInit {

  weathers?: Weathers
  params$!: Subscription
  form!: FormGroup
  userEmail!: string

  constructor(private http: HttpServices, private router: Router) { }

  ngOnInit(): void {
    this.userEmail = JSON.parse(sessionStorage.getItem('user')!)['email']

    this.http.getWeather()
      .then(results => {
        console.info(results)
        this.weathers = results
      })
  }

  setFavorite(area: any) {
    console.info(area.value)
    this.http.setFavWeather(area.value, this.userEmail)
      .then(results => {
        console.info(results)
        this.http.refresh.next(true)
      })
  }
}
