import { Component, input, output, WritableSignal, signal } from '@angular/core';
import { Form, FormGroup } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { TitleCasePipe } from '@angular/common';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'ad-step-recap',
  templateUrl: './ad-step-recap.component.html',
  styleUrls: ['../add-ad.component.scss'],
  imports: [MatIconModule, TitleCasePipe, CurrencyPipe,],
})
export class AdRecapStepComponent {
  
  generalGroup = input<FormGroup>();
  addressGroup = input<FormGroup>();
  detailsGroup = input<FormGroup>();
  uploadedFiles = input.required<File[]>();
  uploadedFileUrls = input<string[]>();
  currentImageIndex = input<number>();

  prevImage = output<void>();
  nextImage = output<void>();
  
  imageSelected = output<number>();
  publish = output<void>();
  mapReady = output<any>();

  //se l'intento è quello configurare un comportamento dopo la pressione del bottone "discard", bisogna utilizzare onDestroy di OnDestroy
  discard = output<any>();
  
}
