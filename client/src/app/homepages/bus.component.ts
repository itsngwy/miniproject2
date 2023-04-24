import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpServices } from '../app.service';
import { Bus } from '../models';
import { MapsAPILoader } from '@agm/core';

@Component({
  selector: 'app-bus',
  templateUrl: './bus.component.html',
  styleUrls: ['./bus.component.css']
})
export class BusComponent implements OnInit {

  form!: FormGroup
  buses: Bus[] = []
  busCode!: string

  longitude!: number
  latitude!: number

  longitude2: number = 103.95279441001836
  latitude2: number = 1.3508023464262278

  zoom: number = 10

  busNames!: any[]
  
  constructor(private fb: FormBuilder, private http: HttpServices, private mapsAPILoader: MapsAPILoader) { }

  ngOnInit(): void {
    // Maybe use this for live location
    // https://www.itsolutionstuff.com/post/angular-google-maps-get-current-locationexample.html
    document.body.style.backgroundImage = "none";
    this.setCurrentLocation()
    this.form = this.createForm()
  }

  private setCurrentLocation() {
    this.http.getLocationService()
      .then(resp=>{
        console.info(resp.lng);
        console.info(resp.lat);
        this.longitude = resp.lng
        this.latitude = resp.lat
      })
  }

  createForm() {
    return this.fb.group({
      busCode: this.fb.control('',Validators.required)
    })
  }

  getBusTiming() {
    this.busCode = this.form.value.busCode
    console.info(this.busCode)
    this.http.getBus(this.busCode)
    .then(results => {
      console.info(results)
      this.buses = results['busDetails']
      this.busCode = results['busStopDetails']
      console.info(this.buses)
    })
  }

  enlarge() {
    let modal = document.getElementById("myModal");
    let captionText = document.getElementById("caption");
    
    modal!.style.display = "block"
    captionText!.innerHTML = "MRT Map";
  }

  // When the user clicks on <span> (x), close the modal
  close(event: any) {
    document.getElementById("myModal")!.style.display = "none";
  }

  // Not using as of now
  onChoseLocation(event: any) {
    console.log(event)
  }

}
