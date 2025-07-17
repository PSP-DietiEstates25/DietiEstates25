
import { Component, signal, input, InputSignal, computed} from '@angular/core';
import { MapComponent, MarkerComponent } from '@maplibre/ngx-maplibre-gl';

import { environmentMap } from '../../environments/environment.map';
import { AutocompleterComponent } from '../autocompleter/autocompleter.component';
import { LngLatLike } from 'maplibre-gl';

@Component({
  selector: 'app-map',
  standalone: true,
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
  imports: [MapComponent, MarkerComponent, AutocompleterComponent],
})
export class Map {

  baseStyle: string = environmentMap.map_osm_bright;

  // Signal “reattivi”
  latitude = signal<number>(environmentMap.initialLatitude);
  longitude = signal<number>(environmentMap.initialLongitude);

  zoom = signal<[number]>([environmentMap.initialZoom]);

  // computed che restituisce sempre [lng, lat]
  center = computed<LngLatLike>(() => [
    this.longitude(),
    this.latitude(),
  ]);

  initialCanvasContextAttribute = signal(environmentMap.initialCanvasContextAttribute);

}
