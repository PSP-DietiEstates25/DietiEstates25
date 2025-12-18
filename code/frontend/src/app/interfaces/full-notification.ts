import { NotificationResponse } from '../services/models';

export interface FullNotification extends NotificationResponse {
  realEstate: string;
}
