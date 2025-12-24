import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges,
  AfterViewInit,
  ElementRef,
  ViewChild,
} from '@angular/core';
import * as L from 'leaflet';
import { environment } from '../../../environments/environment';
import { Point } from '../../interfaces/point';

@Component({
  selector: 'app-results-map',
  template: `<div #resultsMap id="resultsMap"></div>`,
  styles: [
    `
      :host {
        display: block;
        height: 100%;
        min-height: 420px;
      }
      #resultsMap {
        width: 100%;
        height: 100%;
      }
    `,
  ],
})
export class ResultsMapComponent implements AfterViewInit, OnChanges {
  @Input()
  points: Point[] = [];

  @Input()
  center?: { lat: number; lon: number };

  @Input()
  selectedId?: number | null;

  @Output()
  select = new EventEmitter<number>();

  @ViewChild('resultsMap', { static: true })
  mapEl!: ElementRef<HTMLDivElement>;

  private map!: L.Map;
  private markersLayer = L.layerGroup();

  ngAfterViewInit(): void {
    console.log(
      '[ResultsMap] ngAfterViewInit. points:',
      this.points?.length,
      'center:',
      this.center,
    );

    L.Marker.prototype.options.icon = L.icon({
      iconRetinaUrl: 'assets/leaflet/marker-icon-2x.png',
      iconUrl: 'assets/leaflet/marker-icon.png',
      shadowUrl: 'assets/leaflet/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      tooltipAnchor: [16, -28],
      shadowSize: [41, 41],
    });

    const center = this.center ?? { lat: 41.9028, lon: 12.4964 };
    this.map = L.map(this.mapEl.nativeElement, {
      center: [center.lat, center.lon],
      zoom: 12,
      preferCanvas: true,
    });

    const key = environment.geoapifyAPIKey?.trim();
    const useGeoapify = !!key && key.toLowerCase() !== 'secretkey';
    const tileLayer = useGeoapify
      ? L.tileLayer(
          `https://maps.geoapify.com/v1/tile/osm-bright/{z}/{x}/{y}@2x.png?apiKey=${key}`,
          {
            attribution: 'Powered by Geoapify | © OpenStreetMap contributors',
            maxZoom: 20,
          },
        )
      : L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '© OpenStreetMap contributors',
          maxZoom: 19,
        });
    tileLayer.addTo(this.map);

    this.markersLayer.addTo(this.map);

    setTimeout(() => this.map.invalidateSize(), 0);
    new ResizeObserver(() => this.map?.invalidateSize()).observe(
      this.mapEl.nativeElement,
    );

    if (!this.points || this.points.length === 0) {
      console.warn('[ResultsMap] points vuoto: aggiungo 3 marker di test.');
      this.points = [
        { id: 1, lat: 41.9028, lon: 12.4964 },
        { id: 2, lat: 41.89, lon: 12.49 },
        { id: 3, lat: 41.91, lon: 12.5 },
      ];
    }

    this.redrawMarkers();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.map) return;
    if (changes['points']) this.redrawMarkers();
  }

  private redrawMarkers(): void {
    this.markersLayer.clearLayers();
    const bounds = L.latLngBounds([]);
    let added = 0;

    for (const point of this.points ?? []) {
      const latitude = Number(
        point?.lat ?? point?.latitude ?? point?.geographicalPosition?.latitude,
      );

      const longitude = Number(
        point?.lon ??
          point?.longitude ??
          point?.geographicalPosition?.longitude,
      );

      const isValid =
        Number.isFinite(latitude) &&
        Number.isFinite(longitude) &&
        !(latitude === 0 && longitude === 0);

      if (!isValid) {
        if (!environment.production) {
          console.warn(
            '[ResultsMap] scarto punto per lat/lon invalidi:',
            point,
          );
        }
        continue;
      }

      L.marker([latitude, longitude])
        .on(
          'click',
          () => point.id != null && this.select.emit(point.id as number),
        )
        .addTo(this.markersLayer);

      bounds.extend([latitude, longitude]);

      added++;
    }

    if (added > 0) {
      this.map.fitBounds(bounds.pad(0.2));
    }
  }
}
