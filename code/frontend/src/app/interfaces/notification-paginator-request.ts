import { NotificationCategory } from '../enums/notification-category.enum';
import { PaginatorRequest } from './paginator-request';

export interface NotificationPaginatorRequest extends PaginatorRequest {
  categories: NotificationCategory[] | null;
}
