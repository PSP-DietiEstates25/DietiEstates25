import { Component, OnInit, ViewChild, ElementRef, signal, WritableSignal } from '@angular/core';
import * as maplibregl from 'maplibre-gl';

import { AutocompleterComponent } from '../autocompleter/autocompleter.component';
import { environmentMap } from '../../environments/environment.map';

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
  
  map!: WritableSignal<maplibregl.Map>;

  ngOnInit(): void {
    this.initMap();
  }

  private initMap(): void {

    this.map = signal(new maplibregl.Map({
      container: this.mapContainer.nativeElement,
      style: environmentMap.map_osm_bright,
      center: [12.4964, 41.9028], // Roma
      zoom: 12
    }));

    const marker = new maplibregl.Marker()
        .setLngLat([12.550343, 55.665957])
        .addTo(this.map());
  }

}

/*
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import * as maplibregl from 'maplibre-gl';

import { AutocompleterComponent } from '../autocompleter/autocompleter.component';
import { environmentMap } from '../../environments/environment.map';

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
  
  map!: maplibregl.Map;

  ngOnInit(): void {
    this.initMap();
  }

  private initMap(): void {
    this.map = new maplibregl.Map({
      container: this.mapContainer.nativeElement,
      style: environmentMap.map_osm_bright,
      center: [12.4964, 41.9028], // Roma
      zoom: 12
    });

    const marker = new maplibregl.Marker()
        .setLngLat([12.550343, 55.665957])
        .addTo(this.map);
  }

}

*/