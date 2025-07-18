import { Component, ElementRef, AfterViewInit, ViewChild, Input, Output, EventEmitter, effect, WritableSignal, signal } from '@angular/core';
import * as maplibregl from 'maplibre-gl';
import { GeocoderAutocomplete } from '@geoapify/geocoder-autocomplete';
import { environment } from '../../environments/environment';
import { environmentMap } from '../../environments/environment.map';

@Component({
  selector: 'app-autocompleter',
  templateUrl: './autocompleter.component.html',
  styleUrls: ['./autocompleter.component.scss']
})
export class AutocompleterComponent implements AfterViewInit {

  @ViewChild('autocompleteContainer', { static: true })
  autocompleteContainer!: ElementRef;

  private _mapSignal: WritableSignal<maplibregl.Map | null> = signal(null);

  @Input()
  set map(m: maplibregl.Map) {
    this._mapSignal.set(m);
  }

  @Output()
  mapChange = new EventEmitter<maplibregl.Map>();

  private geocoderAutocomplete!: GeocoderAutocomplete;

  private _mapEffect = effect(() => {

    const m = this._mapSignal();
    if (!m) return;

    if (m.loaded()) {
      this.setupMarkerLayer(m);
    } else {
      m.on('load', () => this.setupMarkerLayer(m));
    }
  });

  ngAfterViewInit(): void {
    
    // init Geoapify autocomplete (usa viewChild, quindi qui)
    this.geocoderAutocomplete = new GeocoderAutocomplete(
      this.autocompleteContainer.nativeElement,
      environment.geoapifyAPIKey
    );

    this.geocoderAutocomplete.on('select', ({ properties }: any) => {

      const { lon, lat } = properties;

      const m = this._mapSignal()!;
      m.flyTo({ center: [lon, lat], zoom: 15 });

      const source = m.getSource('marker-source') as maplibregl.GeoJSONSource;
      
      source.setData({
        type: 'FeatureCollection',
        features: [{
          type: 'Feature',
          geometry: { type: 'Point', coordinates: [lon, lat] },
          properties: {}
        }]
      });

      this.mapChange.emit(m);
    });
  }

  private setupMarkerLayer(m: maplibregl.Map) {
    const img = new Image();
    img.crossOrigin = 'anonymous';
    img.src = environmentMap.map_house_marker_layer;
    img.onload = () => {
      if (!m.hasImage('house-icon')) {
        m.addImage('house-icon', img, { pixelRatio: 2 });
      }
      if (!m.getSource('marker-source')) {
        m.addSource('marker-source', {
          type: 'geojson',
          data: { type: 'FeatureCollection', features: [] }
        });
      }
      if (!m.getLayer('marker-layer')) {
        m.addLayer({
          id: 'marker-layer',
          type: 'symbol',
          source: 'marker-source',
          layout: {
            'icon-image': 'house-icon',
            'icon-size': 2,
            'icon-anchor': 'bottom',
            'icon-allow-overlap': true
          }
        });
      }
    };
    img.onerror = e => console.error('Errore caricamento icona:', e);
  }
}