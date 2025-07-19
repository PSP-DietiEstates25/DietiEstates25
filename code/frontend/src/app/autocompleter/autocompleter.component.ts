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

  @Output()
  longitude = new EventEmitter<number>();

  @Output()
  latitude = new EventEmitter<number>();

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

      const {lon, lat} = properties;

      //mi sto fidando che la map non sia null, si presuppone che l'hook effect abbia già eseguito il controllo
      this.flyOnPosition(this._mapSignal()!, lon, lat);
      this.updateMarkerPosition(this._mapSignal()!, lon, lat);
      this.mapChange.emit(this._mapSignal()!);
      this.longitude.emit(lon);
      this.latitude.emit(lat);
    });
  }

  /**
   * Serve per far transire la mappa verso la posizione specificata con una corrispettiva animazione 
   * @param map 
   * @param position 
   */
  private flyOnPosition(map: maplibregl.Map, longitude: number, latitude: number){
    map.flyTo({
      center: [longitude, latitude],
      zoom: environmentMap.initialZoom
    })
  }

  /**
   * Aggiorna il marker alla posizione corrente configurato come nuova source GeoJSON della mappa
   * La funzione updatePositionMarker aggiorna il dato della GeoJSON source creato in setupMarkerLayer, 
   * in modo da riposizionare il marker sulla mappa senza modificare di nuovo il layer o l’icona.
   * @param map
   * @param position
   */
  private updateMarkerPosition(map: maplibregl.Map, longitude: number, latitude: number){

    const source = map.getSource('marker-source') as maplibregl.GeoJSONSource;

    source.setData({
        type: 'FeatureCollection',
        features: [{
          type: 'Feature',
          geometry: { type: 'Point', coordinates: [longitude, latitude] },
          properties: {}
        }]
      });
  }

  private setupMarkerLayer(map: maplibregl.Map) {

  // se è già stato fatto, skip
    if (map.getLayer('marker-layer')) return;

    // 1) registra immagine custom
    const img = new Image();
    img.crossOrigin = 'anonymous';
    img.src = environmentMap.map_house_marker_layer;
    img.onload = () => {
      if (!map.hasImage('house-icon')) {
        map.addImage('house-icon', img, { pixelRatio: 2 });
      }
      // 2) crea source GeoJSON vuota
      map.addSource('marker-source', {
        type: 'geojson',
        data: { type: 'FeatureCollection', features: [] }
      });
      // 3) crea layer symbol
      map.addLayer({
        id: 'marker-layer',
        type: 'symbol',
        source: 'marker-source',
        layout: {
          'icon-image': 'house-icon',
          'icon-size': 1,
          'icon-anchor': 'bottom',
          'icon-allow-overlap': true
        }
      });
    };
    img.onerror = e => console.error('Errore caricamento icona custom:', e);
  }
}