import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../vecchioService/auth/auth.service';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [RouterModule, MatIconModule],
})
export class SidebarComponent implements OnInit {
  isOpen = false;
  userName = '';

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    // niente getUser(), niente oggetto user
    //this.auth.displayName$.subscribe((name: string) => {
      //this.userName = name;
    //});
  }

  toggleSidebar() {
    this.isOpen = !this.isOpen;
  }
}
