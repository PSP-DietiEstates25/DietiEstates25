import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup, FormControl } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  
  private formBuilder = inject(FormBuilder);
  loginProcessingUrl!: string

  submitted = false;

  loginForm = new FormGroup({
    email: new FormControl('',[Validators.required, Validators.minLength(5)]),
    password: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(15)])
  });

  constructor(){
    this.loginProcessingUrl = environment.loginProcessingUrl;
  }

  get email(){
    return this.loginForm.get('email');
  }

  get password(){
    return this.loginForm.get('password');
  }

  onSubmit(event: Event) {

    //Prevenzione del comportamento nativo
    event.preventDefault();
    this.submitted = true;

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const nativeForm = event.target as HTMLFormElement;
    nativeForm.submit();
  }
}
