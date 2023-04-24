import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpServices } from '../app.service';
import { TrainDetails } from '../models';

@Component({
  selector: 'app-train',
  templateUrl: './train.component.html',
  styleUrls: ['./train.component.css']
})
export class TrainComponent implements OnInit {

  form!: FormGroup
  trainDetails?: TrainDetails

  constructor(private fb: FormBuilder, private http: HttpServices) { }
  
  ngOnInit(): void {
      this.form = this.fb.group({
        trainCode: this.fb.control<any>('CCL', Validators.required)
      })
  }

  getTrainCrowd() {
    let trainCode: string = this.form.value['trainCode']
    console.info(trainCode)
    this.http.getTrainCrowd(trainCode)
      .then(results => {
        console.info(results)
        this.trainDetails = results
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

}
