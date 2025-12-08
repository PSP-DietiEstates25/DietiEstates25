import { PaginatorRequest } from './paginator-request';

export interface VisitPaginatorRequest extends PaginatorRequest {
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED' | null;
}
