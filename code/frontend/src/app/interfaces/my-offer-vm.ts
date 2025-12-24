export type MyOfferVM = {
  id: number;
  amount: number;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED' | 'COUNTERED' | string;
  createdAt?: string | null;
};
