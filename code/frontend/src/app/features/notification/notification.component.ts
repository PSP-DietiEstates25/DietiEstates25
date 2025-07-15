import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../core/services/notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
  imports: [CommonModule]
})
export class NotificationComponent implements OnInit {
  visible = false;
  message = '';

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notificationService.notifications$.subscribe(msg => {
      this.message = msg;
      this.visible = true;

      // Nasconde il toast dopo 3 secondi
      setTimeout(() => {
        this.visible = false;
      }, 3000);
    });
  }

  setFalse(){

  }
}