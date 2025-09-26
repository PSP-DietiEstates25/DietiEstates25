import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../../vecchioService/notification.service';

@Component({
  selector: 'app-notification',
  standalone:true,
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
  imports: []
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