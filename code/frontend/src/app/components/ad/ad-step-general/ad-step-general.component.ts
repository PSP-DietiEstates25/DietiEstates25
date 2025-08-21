import { Component, input, output } from '@angular/core';
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
  styleUrls: ['../add-ad.component.scss'],
  standalone: true,
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
  form = input.required<FormGroup>();

  categories = input<string[]>();

  uploadedFiles = input<File[]>();

  uploadedFileUrls = input<string[]>();

  filesSelected = output<FileList>();

  fileRemoved = output<number>();

  selectFiles(event: Event) {
    const inputEl = event.target as HTMLInputElement;
    if (inputEl.files && inputEl.files.length > 0) {
      this.filesSelected.emit(inputEl.files);
    }
  }

  sendFileRemoved(index: number) {
    this.fileRemoved.emit(index);
  }
}