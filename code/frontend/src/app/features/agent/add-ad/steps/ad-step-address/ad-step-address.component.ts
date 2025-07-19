import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MapComponent } from '../../../../../map/map.component';

@Component({
  selector: 'ad-step-address',
  templateUrl: './ad-step-address.component.html',
  styleUrls: ['../../add-ad.component.scss'],
  standalone: true,
  imports: [
    MatListModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MapComponent,
  ],
})
export class AdAddressStepComponent {
  @Input() formGroup!: FormGroup;
  @Input() addressSuggestions: string[] = [];
  @Output() addressInput = new EventEmitter<void>();
  @Output() addressSelected = new EventEmitter<string>();
  @Output() mapReady = new EventEmitter<any>();

  onLatitude(lat: number) {
    this.formGroup.get('locationCoords.lat')?.setValue(lat);
  }
  onLongitude(lon: number) {
    this.formGroup.get('locationCoords.lng')?.setValue(lon);
  }
  onAddressText(address: string) {
    this.formGroup.get('addressText')?.setValue(address);
  }
}
