import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { WebcamComponent, WebcamImage } from 'ngx-webcam';
import { Subject, Subscription } from 'rxjs';
import { HttpServices } from '../app.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-camera',
  templateUrl: './camera.component.html',
  styleUrls: ['./camera.component.css']
})
export class CameraComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild(WebcamComponent)
  webcamComp!: WebcamComponent
  
  trigger = new Subject<void>()
  sub$!: Subscription

  constructor(private cameraSvc: HttpServices, private router: Router) { }
  
  ngOnInit(): void {
  }

  ngAfterViewInit() {
    console.info('>>> afterViewInit: ', this.webcamComp)

    // This is how we can set the dimensions, [width]="100" [height]="100"
    // this.webcam.width = 100
    // this.webcam.height = 100

    // We create a new Subject and link it, [trigger]="trigger"
    // We are listening for trigger, triggering from snap(), which sends a message to trigger = new Subject<void>()
    // An Observable to trigger image capturing. When it fires, an image will be captured and emitted
    this.webcamComp.trigger = this.trigger

    // We are listening for any snapshot($event), (imageCapture)="snapshot($event)"
    // Whenever an image is captured (i.e. triggered by [trigger]), 
    // the image is emitted via this imageCapture EventEmitter, similar to new Subject<> where any .next() message will be sent there
    // We subscribe to it so that we can retrieve any image sent there
    this.sub$ = this.webcamComp.imageCapture.subscribe(
        (webcamImg: WebcamImage) => {
          // Pass the image to snapshot()
          this.snapshot(webcamImg)
        }
    )
  }

  // When the SNAP button is pressed, this method will send a trigger message
  snap() {
    this.trigger.next()
  }

  // After pic taken 
  snapshot(img: WebcamImage) {
    console.info('imgAsBase64: ', img.imageAsBase64)
    console.info('imgAsDataUrl: ', img.imageAsDataUrl)
    console.info('imgData: ', img.imageData)

    // We send it to CameraService to POST it to digital ocean cloud storage
    this.cameraSvc.imageData = img.imageAsDataUrl
    this.router.navigate(['/home/trafficUpdates'])
  }
  
  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }
}
