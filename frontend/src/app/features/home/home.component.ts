import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { LocationService } from '../../core/service/location.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    MatInputModule,
    MatAutocompleteModule,
    NavbarComponent
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  // Selezioni per i menu a tendina
  contractOptions: string[] = ['Rent', 'Buy'];
  estateOptions: string[] = ['Apartment', 'House'];
  roomOptions: string[] = ['1', '2', '3', '4+'];
  priceOptions: string[] = ['< €500', '€500–€1000', '€1000–€1500', '> €1500'];

  // Modelli di binding
  selectedContract!: string;
  selectedEstate!: string;
  selectedRooms!: string;
  selectedPrice!: string;

  // Autocompletamento
  locationInput: string = '';
  locationOptions: string[] = [];

  constructor(private locationService: LocationService) {}

  // Esegue chiamata API quando cambia l'input
  onLocationChange(query: string): void {
    if (query && query.length > 1) {
      this.locationService.search(query).subscribe({
        next: (results) => this.locationOptions = results,
        error: (err) => {
          console.error('Errore nel caricamento delle location:', err);
          this.locationOptions = [];
        }
      });
    } else {
      this.locationOptions = [];
    }
  }

  onSearch(): void {
    console.log('Filtri selezionati:', {
      contract: this.selectedContract,
      estate: this.selectedEstate,
      rooms: this.selectedRooms,
      price: this.selectedPrice,
      location: this.locationInput
    });
    // navigazione verso una pagina di risultati
  }
}