import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationSubject = new Subject<string>();

  // Observable pubblico a cui i componenti possono iscriversi
  get notifications$(): Observable<string> {
    return this.notificationSubject.asObservable();
  }

  // Metodo per inviare una notifica
  show(message: string): void {
    this.notificationSubject.next(message);
  }
}
