import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Map } from 'maplibre-gl';

import { environment } from '../../environments/environment';
import { environmentMap } from '../../environments/environment.map';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit, AfterViewInit {

  @ViewChild('map')
  private mapContainer!: ElementRef<HTMLElement>;

  private map!: Map;

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    const myAPIKey = environment.geoapifyAPIKey;
    const mapStyle = environmentMap.map_klokantech_basic;

    const initialState = {
      lng: 11,
      lat: 49,
      zoom: 4
    };

    this.map = new Map({
      container: this.mapContainer.nativeElement,
      style: `${mapStyle}?apiKey=${myAPIKey}`,
      center: [initialState.lng, initialState.lat],
      zoom: initialState.zoom
    });
  }
}
