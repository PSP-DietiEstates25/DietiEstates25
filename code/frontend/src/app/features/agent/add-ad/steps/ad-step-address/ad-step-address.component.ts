import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'ad-step-address',
  templateUrl: './ad-step-address.component.html',
  styleUrls: ['../../add-ad.component.scss'],
  standalone: true,
  imports: [MatListModule, MatFormFieldModule, ReactiveFormsModule],
})
export class AdAddressStepComponent {
  @Input() formGroup!: FormGroup;
  @Input() addressSuggestions: string[] = [];
  @Output() addressInput = new EventEmitter<void>();
  @Output() addressSelected = new EventEmitter<string>();
  @Output() mapReady = new EventEmitter<any>();
}
