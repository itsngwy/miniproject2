import { AfterViewInit, Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Register } from '../models';
import { HttpServices } from '../app.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css', './loginreg.css']
})
export class RegisterComponent implements OnInit {

  form!: FormGroup
  message!: string;

  constructor(private fb: FormBuilder, private http: HttpServices) { }

  ngOnInit(): void {
    const bodyElement = document.body;
    bodyElement.style.backgroundImage = "url(/assets/images/singapore3.jpg)"
    this.form = this.createForm()
  }

  createForm() {
    return this.fb.group({
      firstName: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      lastName: this.fb.control<string>('', [Validators.required, Validators.minLength(2)]),
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(8), Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}')]),
      repeatpw: this.fb.control<string>('', [Validators.required, Validators.minLength(8), Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}')])
    })
  }

  confirmValidator(controlOne: AbstractControl, controlTwo: AbstractControl) {
    return () => {
      if (controlOne.value !== controlTwo.value)
        return { match_error: 'Value does not match' };
      return null;
    };
  }

  register() {
    const reg = this.form.value as Register
    console.info(reg)
    this.http.register(reg)
      .then(results => {
        console.info('Results: ', results)
        this.message = results['Response']
        this.ngOnInit()
      })
  }

  showPassword(password: any) {
    password.type = password.type === 'password' ? 'text': 'password'
  }

}
