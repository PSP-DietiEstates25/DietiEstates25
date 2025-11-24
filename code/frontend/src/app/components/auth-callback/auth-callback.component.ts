import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../manual_services/auth.service';
import { Router } from '@angular/router';
import { LocalStorageService } from '../../manual_services/local-storage.service';

@Component({
  selector: 'app-auth-callback',
  imports: [],
  templateUrl: './auth-callback.component.html',
  styleUrl: './auth-callback.component.scss'
})
export class AuthCallbackComponent implements OnInit {

  private readonly authService = inject(AuthService);
  private readonly localStorageService = inject(LocalStorageService)
  private readonly routerService = inject(Router);

  ngOnInit(): void {
    this.authService.getUserInfo().subscribe({
      next: (userInfo) => {
        this.authService.setUserInfo({email: userInfo.sub, role: userInfo.role[0]});
        this.localStorageService.setItem('role', userInfo.role[0]);
        this.localStorageService.setItem('isAuthenticated', 'true');
        this.routerService.navigateByUrl('/');
      },
      error: (error) => {
        this.localStorageService.removeItem('isAuthenticated');
        this.routerService.navigateByUrl('/');
      }
    })
  }
}
