import { Role } from './role';

export interface AdminUser {
  id: number;
  email: string;
  role: Role;
  active: boolean;
  createdAt?: string;
}
