import {
  Component,
  ElementRef,
  OnDestroy,
  AfterViewInit,
  ViewChild
} from '@angular/core';
import { Loader } from '@googlemaps/js-api-loader';
import { environment } from '../../environments/environment';


@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  templateUrl: './map.component.html',
  styleUrl: './map.component.scss'
})
export class MapComponent implements AfterViewInit {

  @ViewChild('map', { static: true }) 
  mapElement!: ElementRef<HTMLElement>;
  
  private map!: google.maps.Map;

  private loader = new Loader({
    apiKey: environment.mapsPlatformAPIKey,
    version: 'weekly',
    libraries: ['places']  // se ti serve anche Places
  });

  async ngAfterViewInit(): Promise<void> {
    try {
      // carica *solo* la libreria 'maps' e ne estrae la classe Map
      const { Map } = await this.loader.importLibrary('maps');  
      // inizializza
      this.map = new Map(this.mapElement.nativeElement, {
        center: { lat: 45.4642, lng: 9.1900 },
        zoom: 12
      });
    } catch (err) {
      console.error('Google Maps failed to load', err);
    }
  }

}