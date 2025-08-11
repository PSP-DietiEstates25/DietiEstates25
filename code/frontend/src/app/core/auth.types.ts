export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  role: 'user' | 'admin' | 'estate_agent';
  subjectType: 'USER' | 'STAFFER';
}
