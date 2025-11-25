export interface Comune {
  nome: string;
  codice: string; // Codice ISTAT
  zona: {
    codice: string;
    nome: string;
  };
  regione: {
    codice: string;
    nome: string;
  };
  provincia: {
    codice: string;
    nome: string;
  };
  sigla: string;
  codiceCatastale: string;
  cap: string[];
  popolazione: number;
}