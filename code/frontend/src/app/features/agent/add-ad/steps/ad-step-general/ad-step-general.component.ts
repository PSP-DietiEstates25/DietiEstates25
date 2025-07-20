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

  formGroup = input<FormGroup>();

  //cateogries = input
  categories = input<string[]>();
  uploadedFiles = input<File[]>();
  uploadedFileUrls = input<string[]>();

  filesSelected = output<FileList>();
  fileRemoved = output<number>();

  selectFiles(collectedFilesEvent: FileList) {
    this.filesSelected.emit(collectedFilesEvent);
  }

  sendFileRemoved(index: any){
    this.fileRemoved.emit(index);
  }
}
