import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

// Material Modules
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    // Angular Material
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatTabsModule,
    MatDividerModule
  ],

  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
})
export class AuthComponent implements OnInit {
  // Form reactive
  loginForm!: FormGroup;
  registerForm!: FormGroup;

  // Visibilità password
  hideLoginPassword = true;
  hideRegisterPassword = true;
  hideRegisterConfirm = true;

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    // Form di login: email + password
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    // Form di registrazione: username + email + password + conferma password
    this.registerForm = this.fb.group(
      {
        username: ['', [Validators.required, Validators.minLength(3)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', [Validators.required]],
      },
      {
        validators: this.passwordMatchValidator('password', 'confirmPassword'),
      }
    );
  }

  // Validator custom per check password === confirmPassword
  private passwordMatchValidator(passKey: string, confirmKey: string) {
    return (group: FormGroup) => {
      const pass = group.controls[passKey];
      const confirm = group.controls[confirmKey];
      if (pass.value !== confirm.value) {
        confirm.setErrors({ notMatching: true });
      } else {
        confirm.setErrors(null);
      }
    };
  }

  // * LOGIN *
  onLogin(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { email, password } = this.loginForm.value;
    // TODO → sostituisci con il tuo AuthService:
    // this.authService.loginWithEmail(email, password).subscribe( … )
    console.log('[DUMMY] Effettuo login con:', { email, password });

    // Simulo redirect “in base al ruolo” (ad esempio “Agent” o “Client”):
    // scorri la risposta nel backend e vedi resp.role
    const fintoRuolo: string = 'CLIENT'; // oppure “AGENT” o “ADMIN”
    if (fintoRuolo === 'AGENT') {
      this.router.navigate(['/agent']);
    } else if (fintoRuolo === 'ADMIN') {
      this.router.navigate(['/admin']);
    } else {
      this.router.navigate(['/']);
    }
  }

  // * REGISTRAZIONE *
  onRegister(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const { username, email, password } = this.registerForm.value;
    // TODO → sostituisci con il tuo AuthService:
    // this.authService.register({ username, email, password }).subscribe( … )
    console.log('[DUMMY] Effettuo registrazione con:', {
      username,
      email,
      password,
    });

    // Dopo la registrazione:
    // 1) Login automatico, salva token e redirect
    // 2) Messaggio conferma, rimani sulla tab “login”
    // 3) Redirect

    // Per esempio, dopo registrazione vado su login:
    alert('Registrazione avvenuta con successo! Ora puoi fare il login.');
  }

  // Helper per controllo errori login
  get lf() {
    return this.loginForm.controls;
  }
  // Helper per controllo errori register
  get rf() {
    return this.registerForm.controls;
  }
}
