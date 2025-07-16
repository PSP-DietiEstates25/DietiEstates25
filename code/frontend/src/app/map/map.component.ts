
import { Component } from '@angular/core';
import { MapComponent, MarkerComponent } from '@maplibre/ngx-maplibre-gl';

import { environmentMap } from '../../environments/environment.map';

@Component({
  selector: 'app-map',
  standalone: true,
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
  imports: [MapComponent, MarkerComponent],
})
export class Map {

  style: string = environmentMap.map_osm_bright;

  initialLatitude: number = environmentMap.initialLatitude;
  initialLongitude: number = environmentMap.initialLongitude;
  initialZoom: [number] = [environmentMap.initialZoom];
  initialCenter: [number, number] = [environmentMap.initialLongitude, environmentMap.initialLatitude];

  initialCanvasContextAttribute = environmentMap.initialCanvasContextAttribute;

  indirizzoValentina: [number, number] = [14.367754323, 40.784007384];
  indirizzoGiovanni: [number, number] = [14.2420118, 40.85155];
  indirizzoRoberto: [number, number] = [14.209218, 40.926221];
  indirizzoLuca: [number, number] = [14.247382014, 40.872010897]

  indirizzi = [
    this.indirizzoValentina,
    this.indirizzoGiovanni,
    this.indirizzoRoberto,
    this.indirizzoLuca
  ];
}
