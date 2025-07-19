import { Component, Input, Output, EventEmitter, output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { TitleCasePipe } from '@angular/common';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'ad-step-recap',
  templateUrl: './ad-step-recap.component.html',
  styleUrls: ['../../add-ad.component.scss'],
  imports: [MatIconModule, TitleCasePipe, CurrencyPipe],
})
export class AdRecapStepComponent {
  @Input() generalGroup!: FormGroup;
  @Input() addressGroup!: FormGroup;
  @Input() detailsGroup!: FormGroup;
  @Input() uploadedFiles: File[] = [];
  @Input() uploadedFileUrls: string[] = [];
  @Input() currentImageIndex!: number;
  @Output() prevImage = new EventEmitter<void>();
  @Output() nextImage = new EventEmitter<void>();
  @Output() imageSelected = new EventEmitter<number>();
  @Output() publish = new EventEmitter<void>();
  @Output() mapReady = new EventEmitter<any>();
  @Output() discard = new EventEmitter<any>();
}
