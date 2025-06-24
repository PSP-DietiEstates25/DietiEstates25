import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../core/services/auth.service';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [CommonModule, RouterModule, MatIconModule],
})
export class SidebarComponent implements OnInit {
  isOpen = false;
  userName = '';

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    // supponendo che getUser() ritorni un Observable<{ name: string }>
    this.auth.getUser().subscribe(user => {
      this.userName = user.name;
    });
  }

  toggleSidebar() {
    this.isOpen = !this.isOpen;
  }
}