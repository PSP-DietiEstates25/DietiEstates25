export interface PositionDraft {
  address: string;
  region: string;
  city: string;
  municipality: string;
  latitude: number;
  longitude: number;
  radius?: number;
}
