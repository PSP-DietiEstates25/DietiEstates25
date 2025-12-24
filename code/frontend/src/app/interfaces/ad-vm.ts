import { ProximityTag } from './proximity-tag';

export type AdVM = {
  realEstateId: number;
  detailId: number;
  title: string;
  description?: string | null;
  price?: number | null;
  city?: string | null;
  surface?: number | null;
  rooms?: number | null;

  // mantenuti
  type?: string | null;
  floor?: number | null;
  energyClass?: string | null;

  images: string[];
  coverUrl?: string;
  agent?: { email?: string | null };
  position?: {
    latitude?: number | null;
    longitude?: number | null;
    address?: string | null;
    municipality?: string | null;
  };

  proximityTags?: ProximityTag[];
  utilities?: string[];
};
