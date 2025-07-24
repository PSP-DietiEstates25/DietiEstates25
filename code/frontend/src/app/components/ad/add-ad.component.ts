import { Component, OnInit, signal, WritableSignal } from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';

import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
 
import { LocationService } from '../../services/location.service';

import { MetadataService } from '../../services/metadata.service';
import { ServiceDTO } from '../../interfaces/service-dto';
import { ServiceToggle } from '../../interfaces/service-toggle';

import { AdService } from '../../services/ad.service';

import { AdRecapStepComponent } from '..//../components/ad-step-recap/ad-step-recap.component';
import { AdGeneralStepComponent } from '../ad-step-general/ad-step-general.component';
import { AdAddressStepComponent } from '../ad-step-address/ad-step-address.component';
import { AdDetailsStepComponent } from '../ad-step-details/ad-step-details.component';

import { AdCategory } from '../../enums/ad-category.enum';

@Component({
  selector: 'app-add-ad',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatSelectModule,
    MatCheckboxModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    AdRecapStepComponent,
    AdGeneralStepComponent,
    AdAddressStepComponent,
    AdDetailsStepComponent,
  ],
  templateUrl: './add-ad.component.html',
  styleUrls: ['./add-ad.component.scss'],
})
export class AddAdComponent implements OnInit {

  adForm!: FormGroup;

  /*
  // Nuove variabili: categorie e servizi vengono popolate dinamicamente
  // se vengono popolate dinamicamente (fetchate dal backend) devono essere inserite in ngOnInit
  categories: string[] = ['Ciao'];
  servicesList: ServiceDTO[] = [];

  addressSuggestions: string[] = [];
  uploadedFiles: File[] = [];
  uploadedFileUrls: string[] = [];
  */
  categories!: string[];

  //prima era ServiceDTO
  servicesList!: ServiceToggle[];
  addressSuggestions!: string[];
  uploadedFiles!: File[];
  uploadedFileUrls!: string[];
  currentImageIndex!: number; //carousel

  constructor(
    private formBuilder: FormBuilder,
    private locationService: LocationService,
    private metadataService: MetadataService,
    private adService: AdService,
    private cd: ChangeDetectorRef,
    private router: Router
  ) {
    //deve essere utilizzato solo per iniettare services
  }

  ngOnInit(): void {

    //fetchate dal backend, vedere riga 67
    this.categories = ['Ciao'];
    this.servicesList = [];
    this.addressSuggestions = [];
    this.uploadedFiles = [];
    this.uploadedFileUrls = [];
    this.currentImageIndex = 0;



    // Costruiamo il form:
    // - category viene inizialmente impostato a '' (vuoto) perché la popoleremo dal backend.
    // - features (services) inizialmente empty, ma li gestiremo come FormArray via checkbox dinamiche.
    this.adForm = this.formBuilder.group({
      general: this.formBuilder.group({
        photos: [[]],
        price: [null, [Validators.required, Validators.min(0)]],
        category: ['', Validators.required],
        description: ['', [Validators.required, Validators.minLength(10)]],
      }),
      address: this.formBuilder.group({
        addressText: ['', Validators.required],
        locationCoords: this.formBuilder.group({
          lat: [null, Validators.required],
          lng: [null, Validators.required],
        }),
      }),
      details: this.formBuilder.group({
        squareMeters: [null, [Validators.required, Validators.min(1)]],
        rooms: [null, [Validators.required, Validators.min(1)]],
        floor: [null, Validators.required],
        energyClass: ['', Validators.required],
        // Qui memorizzeremo un array di ID di servizi selezionati
        services: this.formBuilder.control<number[]>([]),
      }),
    });
  }
  

  get generalGroup() {
    return this.adForm.get('general') as FormGroup;
  }
  get addressGroup() {
    return this.adForm.get('address') as FormGroup;
  }
  get detailsGroup() {
    return this.adForm.get('details') as FormGroup;
  }

  // Gestione upload file
  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files) {
      return;
    }
    for (let i = 0; i < input.files.length; i++) {
      this.uploadedFiles.push(input.files[i]);
      this.uploadedFileUrls.push(URL.createObjectURL(input.files[i]));
    }
    this.adForm.get('general.photos')!.setValue(this.uploadedFiles);
    this.cd.detectChanges();
  }

  removeFile(index: number) {
    this.uploadedFiles.splice(index, 1);
    this.uploadedFileUrls.splice(index, 1);
    this.adForm.get('general.photos')!.setValue(this.uploadedFiles);
    if (this.currentImageIndex >= this.uploadedFiles.length) {
      this.currentImageIndex = Math.max(this.uploadedFiles.length - 1, 0);
    }
    this.cd.detectChanges();
  }
  /**
   * Al click su una checkbox di un servizio, aggiorna l'array di ID
   */
  onServiceToggle(serviceId: number, checked: boolean) {
    const servicesArray = this.adForm.get('details.services')!
      .value as number[];
    if (checked) {
      // aggiungo l'ID nella lista
      this.adForm
        .get('details.services')!
        .setValue([...servicesArray, serviceId]);
    } else {
      // rimuovo l'ID
      this.adForm
        .get('details.services')!
        .setValue(servicesArray.filter((id) => id !== serviceId));
    }
  }

  onSubmit() {
    if (this.adForm.invalid) {
      this.adForm.markAllAsTouched();
      return;
    }
    const payload = this.adForm.value;
    this.adService.createAd(payload).subscribe({
      next: (result) => {
        console.log('Annuncio creato con successo:', result);
      },
      error: (err) => {
        console.error('Errore nella creazione annuncio:', err);
      },
    });
  }

  // Carousel helpers 
  get currentImageUrl(): string {
    if (!this.uploadedFiles.length) return '';
    return this.fileToObjectURL(this.uploadedFiles[this.currentImageIndex]);
  }

  prevImage() {
    this.currentImageIndex =
      (this.currentImageIndex - 1 + this.uploadedFiles.length) %
      this.uploadedFiles.length;
  }
  nextImage() {
    this.currentImageIndex =
      (this.currentImageIndex + 1) % this.uploadedFiles.length;
  }

  fileToObjectURL(file: File): string {
    return URL.createObjectURL(file);
  }

  // Submit finale
  onPublish() {
    const payload = this.adForm.value;
    // this.adService.createAd(payload).subscribe(...)
    console.log('Publishing AD:', payload);
  }

  // Discard del form
  onDiscard() {
  if (confirm('Sei sicuro di voler scartare l\'annuncio?')) {
    this.adForm.reset();
    this.uploadedFiles = [];
    this.uploadedFileUrls = [];
    this.currentImageIndex = 0;
    // volendo reindirizzamento a /agent
    this.router.navigate(['/agent']);
  }
}

}
