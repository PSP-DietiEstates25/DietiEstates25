export type AppRole = 'CLIENT' | 'AGENT' | 'ADMIN';

export function mapBackendRoleToApp(role?: string | null): AppRole | null {
  if (!role) return null;
  switch (role) {
    case 'user':
      return 'CLIENT';
    case 'estate_agent':
      return 'AGENT';
    case 'admin':
      return 'ADMIN';
    default:
      return null;
  }
}
