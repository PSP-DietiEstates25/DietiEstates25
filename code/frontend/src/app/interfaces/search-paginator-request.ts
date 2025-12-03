import { PaginatorRequest } from './paginator-request';

export interface SearchPaginatorRequest extends PaginatorRequest {
  category: string;
}
