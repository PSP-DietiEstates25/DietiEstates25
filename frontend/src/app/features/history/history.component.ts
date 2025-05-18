import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NavbarComponent,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    MatInputModule,
    MatIconModule
  ],
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent {
  contractOptions = ['Rent', 'Buy'];
  estateOptions = ['Apartment', 'House'];
  roomOptions = ['1', '2', '3', '4+'];
  priceOptions = ['< €500', '€500–€1000', '€1000–€1500', '> €1500'];
  energyOptions = ['A++', 'A+', 'A', 'B', 'C', 'D'];

  filters = {
    contract: '',
    estate: '',
    rooms: '',
    price: '',
    energy: ''
  };

  submitted = false;
  results: any[] = [];

  onSearch() {
    this.submitted = true;
    this.results = []; // Nessun dato per ora
  }
}