import { Component } from '@angular/core';
import { LoginRequest } from '../../services/models';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest: LoginRequest = {email: '', password: ''};
  errorMessage: Array<String> = [];
}
