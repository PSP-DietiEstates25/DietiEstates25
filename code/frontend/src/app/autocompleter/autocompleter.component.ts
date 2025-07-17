import { Component, ElementRef, ViewChild } from '@angular/core';
import { GeocoderAutocomplete, GeocoderAutocompleteOptions } from '@geoapify/geocoder-autocomplete';

import { environment } from '../../environments/environment';

@Component({
  selector: 'app-autocompleter',
  standalone: true,
  imports: [],
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
    this.geocoderAutocomplete = new GeocoderAutocomplete(container, environment.geoapifyAPIKey, options);
  }
}
