// src/app/app.component.ts
import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { manualConfig } from './config/auth/manualConfig';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  private oauthService = inject(OAuthService);

  async ngOnInit(): Promise<void> {
    this.oauthService.configure(manualConfig);
    await this.oauthService.loadDiscoveryDocumentAndTryLogin();
    this.oauthService.setStorage(sessionStorage);
  }
}