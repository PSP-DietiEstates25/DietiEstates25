export interface CadastralDraft {
  price: number;
  rooms: number;
  floor: number;
  energyClass: 'A4' | 'A3' | 'A2' | 'A1' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G';
  squareMeters: number;
}
