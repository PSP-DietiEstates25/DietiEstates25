import {
  Component,
  ViewChild,
  ElementRef,
  Output,
  signal,
  WritableSignal,
  EventEmitter,
  AfterViewInit,
  OnDestroy,
} from '@angular/core';
import * as maplibregl from 'maplibre-gl';

import { environmentMap } from '../../../environments/environment.map';
import { AutocompleterComponent } from '../autocompleter/autocompleter.component';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [AutocompleterComponent],
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
})
export class MapComponent implements AfterViewInit, OnDestroy {
  @ViewChild('mapContainer', { static: true })
  mapContainer!: ElementRef;

  map = signal<maplibregl.Map | null>(null); // inizialmente null

  @Output()
  latitude = new EventEmitter<number>();

  @Output()
  longitude = new EventEmitter<number>();

  @Output() addressText = new EventEmitter<string>();

  ngAfterViewInit(): void {
    const m = new maplibregl.Map({
      container: this.mapContainer.nativeElement,
      style: environmentMap.map_osm_bright,
      center: [12.4964, 41.9028], // Roma
      zoom: 12,
    });

    this.map.set(m);
  }

  // chiama .remove() sull’istanza MapLibre per liberare risorse e listener
  ngOnDestroy() {
    if (this.map()) this.map()!.remove();
  }

  onLatitude(lat: number) {
    this.latitude.emit(lat);
  }
  onLongitude(lon: number) {
    this.longitude.emit(lon);
  }
  onAddressText(address: string) {
    this.addressText.emit(address);
  }
}
