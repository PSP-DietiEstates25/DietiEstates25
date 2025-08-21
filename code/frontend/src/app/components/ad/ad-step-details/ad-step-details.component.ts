import { Component, input, output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';

import { ServiceToggle } from '../../../interfaces/service-toggle';

@Component({
  selector: 'ad-step-details',
  templateUrl: './ad-step-details.component.html',
  styleUrls: ['../add-ad.component.scss'],
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatCheckboxModule,
    ReactiveFormsModule,
    MatInputModule,
  ],
})
export class AdDetailsStepComponent {
  formGroup = input.required<FormGroup>();
  servicesList = input<ServiceToggle[]>();

  serviceToggle = output<[number, boolean]>();
}
