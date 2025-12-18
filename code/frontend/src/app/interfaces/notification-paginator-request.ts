import { PaginatorRequest } from './paginator-request';

export interface NotificationPaginatorRequest extends PaginatorRequest {
  category: 'NEW_PROPERTIES' | 'PROMOTIONAL' | 'VISIT' | 'OFFER' | null;
}
