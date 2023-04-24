import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CanLeave, LoginDetails, UserAuthenticated } from '../models';
import { HttpServices } from '../app.service';
import { Router } from '@angular/router';
import { UserRepository } from '../user.repository';
import { environment } from "../../environments/environment";
import { getMessaging, getToken } from "firebase/messaging";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css', './loginreg.css']
})
export class LoginComponent implements OnInit, CanLeave {

  form!: FormGroup
  userAuth!: UserAuthenticated
  loginFailMsg!: string
  message:any = null;

  // For me to keep track which component it is in
  stringInWhichTs: string = 'From: login.component.ts'

  canLeavePage: boolean = false

  constructor(private fb: FormBuilder, private http: HttpServices, private router: Router, private userRepo: UserRepository) { }

  ngOnInit(): void {
      document.body.style.backgroundImage = "url(/assets/images/singapore3.jpg)";
      this.form = this.createLoginForm()
      sessionStorage.removeItem('user')
  }

  createLoginForm(): FormGroup {
    return this.fb.group({
      email: this.fb.control<string>('ngwy17@gmail.com', [Validators.required, Validators.email]),
      password: this.fb.control<string>('Abc@123!#', [Validators.required])
    })
  }

  showPassword(password: any) {
    // We are actually taking in the Input box and check the type of it
    // If it is type password, means true and it will change to text
    // If it is type text, means its false and it will change to password
    password.type = password.type === 'password' ? 'text' : 'password';
  }

  login() {
    const loginDetails: LoginDetails = this.form.value
    // console.info(loginDetails)
    
    this.http.userLogin(loginDetails)
      .then(results => {
        this.userAuth = results
        if (this.userAuth.authentication == true) {
          // this.canLeavePage = true;
          this.addUserDetailsAfterAuthentication(this.userAuth)
          this.requestPermission(loginDetails.email)
          this.router.navigate(['/home'])
        } else {
          this.addUserDetailsAfterAuthentication(this.userAuth)
          this.loginFailMsg = 'Login unsuccessful. Please try again'
        }
      })
  }

  addUserDetailsAfterAuthentication(userAuth: UserAuthenticated) {
    // Get the values from form
    console.info('>>> User Login Details: ', userAuth)
    sessionStorage.setItem('user', JSON.stringify(userAuth))
  }

  // We implemented CanLeave into HomeComponent so it will auto call canLeave() method when we are trying to leave this path
  // If the form is valid, means the Validators in form is fulfilled
  // We will then return true and leave
  canLeave(): boolean {
    console.info(">>> canLeave() is called.")
    return this.canLeavePage
  }

  requestPermission(email: string) {
    const messaging = getMessaging();
    getToken(messaging, 
     { vapidKey: environment.firebase.vapidKey }).then(
       (currentToken) => {
         if (currentToken) {
           console.log("Hurraaa!!! we got the token.....");
           console.log(currentToken);
           this.http.postKey(email, currentToken)
         } else {
           console.log('No registration token available. Request permission to generate one.');
         }
     }).catch((err) => {
        console.log('An error occurred while retrieving token. ', err);
    });
  }

}
