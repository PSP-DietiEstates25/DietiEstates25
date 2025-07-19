import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatRadioModule } from '@angular/material/radio';
import { TitleCasePipe } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'ad-step-general',
  templateUrl: './ad-step-general.component.html',
  styleUrls: ['../../add-ad.component.scss'],
  imports: [
    MatIconModule,
    MatFormFieldModule,
    MatRadioModule,
    TitleCasePipe,
    ReactiveFormsModule,
    MatInputModule,
  ],
})
export class AdGeneralStepComponent {
  @Input() formGroup!: FormGroup;
  @Input() categories: string[] = [];
  @Input() uploadedFiles: File[] = [];
  @Input() uploadedFileUrls: string[] = [];
  @Output() fileSelected = new EventEmitter<Event>();
  @Output() fileRemoved = new EventEmitter<number>();
}
