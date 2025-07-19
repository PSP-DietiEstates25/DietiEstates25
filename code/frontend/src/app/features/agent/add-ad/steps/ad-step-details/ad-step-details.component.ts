import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'ad-step-details',
  templateUrl: './ad-step-details.component.html',
  styleUrls: ['../../add-ad.component.scss'],
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatCheckboxModule,
    ReactiveFormsModule,
    MatInputModule
  ],
})
export class AdDetailsStepComponent {
  @Input() formGroup!: FormGroup;
  @Input() servicesList: { id: number; name: string }[] = [];
  @Output() serviceToggle = new EventEmitter<{
    id: number;
    checked: boolean;
  }>();
}
