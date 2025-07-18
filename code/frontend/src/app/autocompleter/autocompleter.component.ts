import { Component, ElementRef, model, AfterViewInit, ViewChild, computed } from '@angular/core';
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

  map = model.required<maplibregl.Map>();

  private geocoderAutocomplete!: GeocoderAutocomplete;

  ngAfterViewInit(): void {

    // 1) init autocomplete
    this.geocoderAutocomplete = new GeocoderAutocomplete(
      this.autocompleteContainer.nativeElement,
      environment.geoapifyAPIKey,
      {
        //options
      }
    );

    if (this.map().loaded()) {
      this.setupMarkerLayer();
    } else {
      this.map().on('load', () => this.setupMarkerLayer());
    }

    // 3) al select, aggiorno la source
    this.geocoderAutocomplete.on('select', (value: any) => {

      const { lon, lat } = value.properties;

      // centro la mappa
      this.map.update(mp => mp.flyTo({ center: [lon, lat], zoom: 15 }));

      // aggiorno la GeoJSON source (con properties richieste da TS)
      const src = this.map().getSource('marker-source') as maplibregl.GeoJSONSource;
      src.setData({
        type: 'FeatureCollection',
        features: [
          {
            type: 'Feature',
            geometry: { type: 'Point', coordinates: [lon, lat] },
            properties: {}
          }
        ]
      });
    });

  }

  private setupMarkerLayer() {
    // carico l’immagine senza forzarne la dimensione
    const img = new Image();
    img.crossOrigin = 'anonymous';
    img.src = environmentMap.map_house_marker_layer;

    img.onload = () => {
      // Registra l’immagine dicendo che è retina (pixelRatio=2)
      if (!this.map().hasImage('house-icon')) {
        this.map().addImage('house-icon', img, { pixelRatio: 2 });
      }

      // il resto identico…
      if (!this.map().getSource('marker-source')) {
        this.map().addSource('marker-source', {
          type: 'geojson',
          data: { type: 'FeatureCollection', features: [] }
        });
      }
      if (!this.map().getLayer('marker-layer')) {
        this.map().addLayer({
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
    img.onerror = e => console.error(e);
  }
}


/*
import { Component, ElementRef, input, Output, EventEmitter, AfterViewInit, ViewChild, InputSignal, computed } from '@angular/core';
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

  map = input.required<maplibregl.Map>();

  @Output()
  mapChange = new EventEmitter<maplibregl.Map>();

  private geocoderAutocomplete!: GeocoderAutocomplete;

  ngAfterViewInit(): void {

    // 1) init autocomplete
    this.geocoderAutocomplete = new GeocoderAutocomplete(
      this.autocompleteContainer.nativeElement,
      environment.geoapifyAPIKey,
      {
        //options
      }
    );

    let es = computed(() => {

    if (this.map().loaded()) {
      this.setupMarkerLayer();
    } else {
      this.map().on('load', () => this.setupMarkerLayer());
    }

    // 3) al select, aggiorno la source
    this.geocoderAutocomplete.on('select', (value: any) => {

      const { lon, lat } = value.properties;

      // centro la mappa
      this.map().flyTo({ center: [lon, lat], zoom: 15 });
      this.mapChange.emit(this.map());

      // aggiorno la GeoJSON source (con properties richieste da TS)
      const src = this.map().getSource('marker-source') as maplibregl.GeoJSONSource;
      src.setData({
        type: 'FeatureCollection',
        features: [
          {
            type: 'Feature',
            geometry: { type: 'Point', coordinates: [lon, lat] },
            properties: {}
          }
        ]
      });
    });

    });

  }

  private setupMarkerLayer() {

    let el = computed(() => {

    // carico l’immagine senza forzarne la dimensione
    const img = new Image();
    img.crossOrigin = 'anonymous';
    img.src = environmentMap.map_house_marker_layer;

    img.onload = () => {
      // Registra l’immagine dicendo che è retina (pixelRatio=2)
      if (!this.map().hasImage('house-icon')) {
        this.map().addImage('house-icon', img, { pixelRatio: 2 });
      }

      // il resto identico…
      if (!this.map().getSource('marker-source')) {
        this.map().addSource('marker-source', {
          type: 'geojson',
          data: { type: 'FeatureCollection', features: [] }
        });
      }
      if (!this.map().getLayer('marker-layer')) {
        this.map().addLayer({
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
    img.onerror = e => console.error(e);

    })
  }
}

*/

