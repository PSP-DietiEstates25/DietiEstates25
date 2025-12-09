export type EnergyClass =
  | 'A4'
  | 'A3'
  | 'A2'
  | 'A1'
  | 'B'
  | 'C'
  | 'D'
  | 'E'
  | 'F'
  | 'G';

export const ENERGY_LABELS: Record<number, string> = {
  0: 'A4',
  1: 'A3',
  2: 'A2',
  3: 'A1',
  4: 'B',
  5: 'C',
  6: 'D',
  7: 'E',
  8: 'F',
  9: 'G',
};
