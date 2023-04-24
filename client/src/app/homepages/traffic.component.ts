import { Component, OnInit } from '@angular/core';
import { HttpServices } from '../app.service';
import { TrafficCameras } from '../models';

@Component({
  selector: 'app-traffic',
  templateUrl: './traffic.component.html',
  styleUrls: ['./traffic.component.css']
})
export class TrafficComponent implements OnInit {

  trafficCameras!: TrafficCameras
  image!: string

  constructor(private http: HttpServices) { }

  ngOnInit(): void {
      this.http.getTrafficImages()
        .then(results => {
          this.trafficCameras = results
          console.info(results)
        })
  }

  enlarge(image: string) {
    this.image = image
    let modal = document.getElementById("myModal");
    let captionText = document.getElementById("caption");
    
    modal!.style.display = "block"
    captionText!.innerHTML = "Traffic Camera";
  }

  // When the user clicks on <span> (x), close the modal
  close(event: any) {
    document.getElementById("myModal")!.style.display = "none";
  }
}
