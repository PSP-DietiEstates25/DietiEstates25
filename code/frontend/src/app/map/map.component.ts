import { Component, OnInit, ViewChild, ElementRef, WritableSignal, signal } from '@angular/core';
import * as maplibregl from 'maplibre-gl';

import { environmentMap } from '../../environments/environment.map';
import { AutocompleterComponent } from '../autocompleter/autocompleter.component';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [AutocompleterComponent],
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  @ViewChild('mapContainer', { static: true })
  mapContainer!: ElementRef;

  // 1) signal condivisa col child
  map!: WritableSignal<maplibregl.Map>;

  ngOnInit(): void {
    this.map = signal(
      new maplibregl.Map({
        container: this.mapContainer.nativeElement,
        style: environmentMap.map_osm_bright,
        center: [12.4964, 41.9028], // Roma
        zoom: 12
      })
    );
  }
}