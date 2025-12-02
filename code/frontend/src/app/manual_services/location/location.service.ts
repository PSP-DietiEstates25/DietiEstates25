import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { map, shareReplay } from 'rxjs';

export interface ComuneEntry {
  nome: string;
  regione: {
    nome: string;
    code?: string;
  }; 
  siglaProvincia: string;
}

@Injectable({
  providedIn: 'root'
})
export class LocationsService {
  private http = inject(HttpClient);

  //carica json regioni italiane (dati ISTAT)
  private italianGeoData = this.http.get<ComuneEntry[]>('/assets/data/comuni.json').pipe(
    shareReplay(1)
  );

  //ottiene le regioni dal json
  getRegions() {
    return this.italianGeoData.pipe(
      map(comune => {
        const regions = [...new Set(comune.map(item => item.regione.nome))];
        return regions.sort();
      })
    );
  }

  //ottiene le città dal json data una regione
  getCitiesByRegion(regionName: string) {
    return this.italianGeoData.pipe(
      map(comune => {
        return comune
          .filter(item => item.regione.nome === regionName)
          .map(item => item.nome)
          .sort();
      })
    );
  }
}