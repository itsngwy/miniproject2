import { Component, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpServices } from '../app.service';
import { FileUpload, TrafficImages } from '../models';
import { ViewChild } from '@angular/core';

@Component({
  selector: 'app-trafficupdates',
  templateUrl: './trafficupdates.component.html',
  styleUrls: ['./trafficupdates.component.css']
})
export class TrafficUpdatesComponent implements OnInit {

  file!: File

  form!: FormGroup
  trafficImages!: TrafficImages[]
  userEmail!: string

  @ViewChild('fileUpload')
  myInputVariable!: ElementRef;

  picDisable: boolean = false
  imageData = "";
  blob!: Blob

  constructor (private fb: FormBuilder, private http: HttpServices) { }

  ngOnInit(): void {
    this.userEmail = JSON.parse(sessionStorage.getItem('user')!)['email']
    this.createForm()
    this.getTrafficImages()
    this.imageData = this.http.imageData
    console.info(this.imageData)
  }

  createForm() {
    this.form = this.fb.group({
      fileUpload: this.fb.control(Validators.required),
      title: this.fb.control<string>('', Validators.required),
      description: this.fb.control<string>('', Validators.required)
    })
  }

  onFileSelected(event: any) {
    this.file = event.target.files[0];
    console.info(this.file)
    this.form.patchValue({
      fileUpload: this.file
    })
    if (this.file != null) {
      this.picDisable = true
    }
  }

  upload() {
    //console.info(this.file)
        
    const formData: FileUpload = this.form.value
    console.info(formData)

    if (this.imageData.length > 0) {
      this.blob = this.dataURItoBlob(this.imageData)
      formData.fileUpload = this.blob
    }

    this.http.uploadFile(formData, this.userEmail)
      .then(results => {
        this.http.imageData = ""
        this.ngOnInit()
      })
    console.info(this.userEmail)
  }

  getTrafficImages() {
    this.http.getUsersTrafficImages()
      .then(results => {
        console.info(results)
        this.trafficImages = results
      })
  }

  clearForm() {
    this.myInputVariable.nativeElement.value = "";
    this.imageData = ""
    this.picDisable = false
    this.createForm()
  }

  // Change long data string uri to Blob for upload
  dataURItoBlob(dataURI: string) {
    // convert base64 to raw binary data held in a string
    // doesn't handle URLEncoded DataURIs - see SO answer #6850276 for code that does this
    var byteString = atob(dataURI.split(',')[1]);

    // separate out the mime/type component, eg image/jpeg
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

    // write the bytes of the string to an ArrayBuffer
    var ab = new ArrayBuffer(byteString.length);
    // Not used
    var ia = new Uint8Array(ab);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    return new Blob([ab], {type: mimeString});
  }
}
