import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { MaterialModule } from '../../shared/material/material.module';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    CommonModule,
    MatSelectModule,
    MatOptionModule
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  contractOptions: string[] = ['Rent', 'Buy'];
  estateOptions: string[] = ['Apartment', 'House'];
  roomOptions: string[] = ['1', '2', '3', '4+'];
  priceOptions: string[] = ['< €500', '€500–€1000', '€1000–€1500', '> €1500'];

  selectedContract!: string;
  selectedEstate!: string;
  selectedRooms!: string;
  selectedPrice!: string;
}