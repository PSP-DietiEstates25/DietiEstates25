import { 
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewInit
} from '@angular/core';

import { 
  Browser,
  Map,
  map,
  tileLayer
} from 'leaflet';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrl: './map.component.scss'
})
export class MapComponent implements OnInit, AfterViewInit{

  @ViewChild('map')
  private mapContainter!: ElementRef<HTMLElement>;

  constructor(){

  };

  ngOnInit(){

  };

  ngAfterViewInit(){

    const initialState = { lng:11, lat:49, zoom:4 };
    const leafLet: Map = map(this.mapContainter.nativeElement).setView([initialState.lat, initialState.lng], initialState.zoom);

    const isRetina = Browser.retina;
    const baseUrl = "https://maps.geoapify.com/v1/tile/osm-bright/{z}/{x}/{y}.png?apiKey={apiKey}";
    const retinaUrl = "https://maps.geoapify.com/v1/tile/osm-bright/{z}/{x}/{y}@2x.png?apiKey={apiKey}";

    tileLayer(isRetina ? retinaUrl : baseUrl, {
      attribution: 'Powered by <a href="https://www.geoapify.com/" target="_blank">Geoapify</a> | <a href="https://openmaptiles.org/" target="_blank">© OpenMapTiles</a> <a href="https://www.openstreetmap.org/copyright" target="_blank">© OpenStreetMap</a> contributors',
      apiKey: '1f761aed5939488687abc6b724996598',
      maxZoom: 20,
      id: 'osm-bright',
    } as any).addTo(leafLet);
  };

}
