import { Status } from '../enums/status.enum';
import { PaginatorRequest } from './paginator-request';

export interface OfferPaginatorRequest extends PaginatorRequest {
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED' | 'COUNTERED' | null;
}
