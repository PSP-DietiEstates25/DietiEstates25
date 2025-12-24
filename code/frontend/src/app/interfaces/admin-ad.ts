export interface AdminAd {
  id: number;
  title: string;
  city?: string | null;
  price?: number | null;
  active?: boolean | null;
  createdAt?: string | null;
}
