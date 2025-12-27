import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../manual_services/auth/auth.service';
import { Router } from '@angular/router';
import { LocalStorageService } from '../../manual_services/local-storage/local-storage.service';
import { map, of, switchMap, tap } from 'rxjs';
import { UserControllerService } from '../../services/services';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-auth-callback',
  imports: [],
  templateUrl: './auth-callback.component.html',
  styleUrl: './auth-callback.component.scss',
})
export class AuthCallbackComponent implements OnInit {
  private authService = inject(AuthService);
  private localStorageService = inject(LocalStorageService);
  private routerService = inject(Router);
  private toastrService = inject(ToastrService)

  private userService = inject(UserControllerService);

  ngOnInit(): void {
  this.authService.getUserInfo()
    .pipe(
      switchMap((userInfo) => {
        if(userInfo.role[0] === "OIDC_USER" || userInfo.role[0] === "USER"){
          return this.userService.registerUser({ 
              body: { email: userInfo.sub } 
          }).pipe(
              //modo alternativo per passare comunque userinfo al posto di register
              map(() => userInfo) 
          );
        } else return of(userInfo);
      })
    )
    .subscribe({
      next: (userInfo) => {
        this.authService.setUserInfo({
          email: userInfo.sub,
          role: userInfo.role[0],
        });
        this.localStorageService.setItem('role', userInfo.role[0]);
        this.localStorageService.setItem('isAuthenticated', 'true');
        
        this.routerService.navigateByUrl('/');
      },
      error: (error) => {
        this.localStorageService.removeItem('isAuthenticated');
        this.localStorageService.removeItem('role');
        this.toastrService.error("Errore durante il login, riprova oppure contatta un admin.", "Errore");
        this.routerService.navigateByUrl('/');
      },
    });
  }
}
