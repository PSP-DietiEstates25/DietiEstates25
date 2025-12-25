import '@geoapify/geocoder-autocomplete/styles/round-borders.css'; 
import * as L from 'leaflet';
import {
  AfterViewInit,
  Component,
  computed,
  Signal,
  ViewChild,
  ElementRef,
  signal,
  output,
  input,
} from '@angular/core';
import { environment } from '../../../environments/environment';
import { GeocoderAutocomplete } from '@geoapify/geocoder-autocomplete';
import { environmentMap } from '../../../environments/environment.map';

@Component({
  selector: 'app-map',
  imports: [],
  templateUrl: './map.component.html',
  styleUrl: './map.component.scss',
})
export class MapComponent implements AfterViewInit {
  @ViewChild('map')
  private mapContainer!: ElementRef<HTMLElement>;

  @ViewChild('autocomplete')
  private autocompleteContainer!: ElementRef<HTMLElement>;

  private map!: L.Map;

  //Naples
  initialLatitude = input<number>(40.85631);
  initialLongitude = input<number>(14.24641);
  initialZoom = input<number>(13);

  private autocompleter!: GeocoderAutocomplete;
  private marker!: L.Marker;
  private markerIcon!: L.Icon;

  private _latitude = signal(this.initialLatitude());
  private _longitude = signal(this.initialLongitude());
  latitude!: Signal<number>;
  longitude!: Signal<number>;
  latitudeChange = output<number>();
  longitudeChange = output<number>();
  isNotEditable = input.required<boolean>();

  constructor() {
    this.latitude = computed(() => this._latitude());
    this.longitude = computed(() => this._longitude());
  }

  ngAfterViewInit() {
    this.setMarkerIcon();
    this.setMap();
    if (!this.isNotEditable()) {
      this.setAutocompleter();
      this.setAutocompleterMarkerEvent();
      this.setClickMapEvents();
    }
  }

  setMap() {
    const initialLatitude = this.initialLatitude();
    const initialLongitude = this.initialLongitude();
    const initialZoom = this.initialZoom();

    this.map = new L.Map(this.mapContainer.nativeElement, {
      zoomControl: false,
    }).setView([initialLatitude, initialLongitude], initialZoom);

    L.control.zoom({ position: 'bottomright' }).addTo(this.map);

    // credits attribution required
    this.map.attributionControl
      .setPrefix('')
      .addAttribution(
        'Powered by <a href="https://www.geoapify.com/" target="_blank">Geoapify</a> | © OpenStreetMap <a href="https://www.openstreetmap.org/copyright" target="_blank">contributors</a>',
      );

    //map style and attribution
    L.tileLayer(environmentMap.map_osm_bright, {
      attribution:
        'Powered by <a href="https://www.geoapify.com/" target="_blank">Geoapify</a> | © OpenStreetMap <a href="https://www.openstreetmap.org/copyright" target="_blank">contributors</a>',
      maxZoom: 20,
    }).addTo(this.map);

    if (this.isNotEditable()) {
      if (this.marker) {
        this.marker.setLatLng([initialLatitude, initialLongitude]);
      } else {
        this.marker = L.marker([initialLatitude, initialLongitude], {
          icon: this.markerIcon,
        }).addTo(this.map);
      }

      this.map.panTo([initialLatitude, initialLongitude]);

      this.setLatitude(initialLatitude);
      this.setLongitude(initialLongitude);

      this.updateLatLon();
    }
  }

  setAutocompleter() {
    this.autocompleter = new GeocoderAutocomplete(
      this.autocompleteContainer.nativeElement,
      environment.geoapifyAPIKey,
    );
  }

  setMarkerIcon() {
    this.markerIcon = L.icon({
      iconUrl: `https://api.geoapify.com/v1/icon/?type=awesome&color=%232ea2ff&size=large&scaleFactor=2&apiKey=${environment.geoapifyAPIKey}`,
      iconSize: [38, 56],
      iconAnchor: [19, 51],
      popupAnchor: [0, -60],
    });
  }

  setAutocompleterMarkerEvent() {
    this.autocompleter.on('select', (location: any) => {
      const lat = location.properties.lat;
      const lon = location.properties.lon;

      if (this.marker) {
        this.marker.setLatLng([lat, lon]);
      } else {
        this.marker = L.marker([lat, lon], {
          icon: this.markerIcon,
          draggable: true,
        }).addTo(this.map);

        this.setDragMarkerEvents();
      }

      this.map.panTo([lat, lon]);

      this.setLatitude(lat);
      this.setLongitude(lon);

      this.updateLatLon();
    });
  }

  setDragMarkerEvents() {
    if (!this.marker) return;

    this.marker.on('drag', () => {});

    this.marker.on('dragend', (dragEndEvent: L.DragEndEvent) => {
      const marker = dragEndEvent.target as L.Marker;
      const lat = marker.getLatLng().lat;
      const lon = marker.getLatLng().lng;

      this.map.panTo([lat, lon]);

      this.setLatitude(lat);
      this.setLongitude(lon);

      this.updateLatLon();
    });
  }

  setClickMapEvents() {
    this.map.on('click', (e: L.LeafletMouseEvent) => {
      const lat = e.latlng.lat;
      const lon = e.latlng.lng;

      if (this.marker) {
        this.marker.setLatLng([lat, lon]);
      } else {
        this.marker = L.marker([lat, lon], {
          icon: this.markerIcon,
          draggable: true,
        }).addTo(this.map);
        this.setDragMarkerEvents();
      }

      this.map.panTo([lat, lon]);

      this.setLatitude(lat);
      this.setLongitude(lon);

      this.updateLatLon();
    });
  }

  setLatitude(latitude: number) {
    this._latitude.set(latitude);
  }

  setLongitude(longitude: number) {
    this._longitude.set(longitude);
  }

  updateLatLon() {
    this.latitudeChange.emit(this.latitude());
    this.longitudeChange.emit(this.longitude());
  }
}
