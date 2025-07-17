import { Component, ElementRef, ViewChild } from '@angular/core';
import { GeocoderAutocomplete, GeocoderAutocompleteOptions } from '@geoapify/geocoder-autocomplete';

import { environment } from '../../environments/environment';

@Component({
  selector: 'app-autocompleter',
  templateUrl: './autocompleter.component.html',
  styleUrl: './autocompleter.component.scss'
})
export class AutocompleterComponent {

  @ViewChild('autocompleteContainer', { static: true })
  autocompleteContainer!: ElementRef;

  private geocoderAutocomplete!: GeocoderAutocomplete;

  ngAfterViewInit(): void {
    const container = this.autocompleteContainer.nativeElement;

    // Customize your Geocoder-Autocomplete options
    const options: GeocoderAutocompleteOptions = {
      // Add your options here
    };

    // Create an instance of Geocoder-Autocomplete
    this.geocoderAutocomplete = new GeocoderAutocomplete(
      container,
      environment.geoapifyAPIKey, 
      options);

      // Al click su un risultato
    this.geocoderAutocomplete.on('select', (value: any) => {
      const { lon, lat } = value.properties;
      //this.map.flyTo({ center: [lon, lat], zoom: 15 });
    });
  }
}
