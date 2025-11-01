import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  SearchFacade,
  Category,
} from '../../../components/search/search.facade';
import { GeographicalPositionRequest } from '../../../services/models/geographical-position-request';
import { UtilityRequest } from '../../../services/models/utility-request';
import { CadastralFilterRequest } from '../../../services/models/cadastral-filter-request';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-filter-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './filter-panel.component.html',
})
export class FilterPanelComponent {
  facade = inject(SearchFacade);

  setCf<K extends keyof CadastralFilterRequest>(
    k: K,
    v: CadastralFilterRequest[K]
  ) {
    this.cf.update((c) => ({ ...c, [k]: coerceNumberIfNeeded(k, v) } as any));
  }
  setGeo<K extends keyof GeographicalPositionRequest>(
    k: K,
    v: GeographicalPositionRequest[K]
  ) {
    this.geo.update((g) => ({ ...g, [k]: coerceNumberIfNeeded(k, v) } as any));
  }
  setUti<K extends keyof UtilityRequest>(k: K, v: UtilityRequest[K]) {
    this.uti.update((u) => ({ ...u, [k]: v } as any));
  }

  category: Category = 'SALE';
  page = 1;
  size = 12;
  userEmail = '';

  geo = signal<GeographicalPositionRequest>({
    city: '',
    municipality: '',
    address: '',
    latitude: 0,
    longitude: 0,
    // radius: (omesso)
  });

  private cleanGeo(
    g: GeographicalPositionRequest
  ): GeographicalPositionRequest {
    const city = (g.city ?? '').trim();
    const municipality = (g.municipality ?? '').trim();
    const addressIn = (g.address ?? '').trim();

    const address = addressIn || city || municipality || '-';

    const out: any = {
      address,
      city,
      municipality,
    };

    const lat = Number(g.latitude);
    const lon = Number(g.longitude);
    if (Number.isFinite(lat) && Number.isFinite(lon)) {
      out.latitude = lat;
      out.longitude = lon;
    }

    const r = Number((g as any).radius);
    if (Number.isFinite(r) && r > 0) out.radius = r;

    return out as GeographicalPositionRequest;
  }

  uti = signal<UtilityRequest>({
    hasAirConditioning: false,
    hasDoorman: false,
    hasElevator: false,

    nearPark: false,
    nearPublicTransport: false,
    nearSchool: false,
  });

  cf = signal<CadastralFilterRequest>({
    minPrice: 0,
    maxPrice: 999999999,
    minSquareMeters: 0,
    maxSquareMeters: 100000,
    minRooms: 0,
    maxRooms: 50,
    minFloor: -10,
    maxFloor: 100,
    minEnergyClass: 0,
    maxEnergyClass: 9,
  });

  apply() {
    this.facade.resetContext();

    const geo = this.cleanGeo(this.geo());
    const uti = this.uti();
    const cf = this.cf();

    this.facade.cacheFilters(geo, uti, cf);

    this.facade
      .prepareDetail(geo, uti)
      .pipe(
        switchMap(() => this.facade.prepareCadastralFilter(cf)),
        switchMap(() =>
          this.facade.search({
            category: this.category,
            page: this.page,
            size: this.size,
            userEmail: this.userEmail,
          })
        )
      )
      .subscribe({ error: () => {} });
  }
}

function coerceNumberIfNeeded<K>(k: K, v: any) {
  return typeof v === 'string' && v.trim() !== '' && !Number.isNaN(+v) ? +v : v;
}
