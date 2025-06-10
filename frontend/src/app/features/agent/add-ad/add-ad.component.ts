import {
  Component,
  OnInit,
  AfterViewInit,
  ViewChild,
  ElementRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators
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
import * as L from 'leaflet';
import { LocationService, Coords } from '../../../core/services/location.service';
import { MetadataService, ServiceDto } from '../../../core/services/metadata.service';

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
    MatListModule
  ],
  templateUrl: './add-ad.component.html',
  styleUrls: ['./add-ad.component.scss']
})
export class AddAdComponent implements OnInit, AfterViewInit {
  @ViewChild('mapContainer', { static: false }) mapEl!: ElementRef<HTMLDivElement>;
  @ViewChild('recapMapContainer', { static: false }) recapMapEl!: ElementRef<HTMLDivElement>;

  adForm!: FormGroup;

  // Nuove variabili: categorie e servizi vengono popolate dinamicamente
  categories: string[] = [];
  servicesList: ServiceDto[] = [];

  addressSuggestions: string[] = [];
  uploadedFiles: File[] = [];

  // carousel
  currentImageIndex = 0;

  // leaflet
  private map!: L.Map;
  private recapMap!: L.Map;
  private marker!: L.Marker;

  constructor(
    private fb: FormBuilder,
    private locationService: LocationService,
    private metadataService: MetadataService
  ) {}

  ngOnInit(): void {
    // Costrusciamo il form: 
    // - category viene inizialmente impostato a '' (vuoto) perché la popoleremo dal backend.
    // - features (services) inizialmente empty, ma li gestiremo come FormArray via checkbox dinamiche.
    this.adForm = this.fb.group({
      general: this.fb.group({
        photos: [[]],
        price: [null, [Validators.required, Validators.min(0)]],
        category: ['', Validators.required],
        description: ['', [Validators.required, Validators.minLength(10)]]
      }),
      address: this.fb.group({
        addressText: ['', Validators.required],
        locationCoords: this.fb.group({
          lat: [null, Validators.required],
          lng: [null, Validators.required]
        })
      }),
      details: this.fb.group({
        squareMeters: [null, [Validators.required, Validators.min(1)]],
        rooms: [null, [Validators.required, Validators.min(1)]],
        floor: [null, Validators.required],
        energyClass: ['', Validators.required],
        // Qui memorizzeremo un array di ID di servizi selezionati
        services: this.fb.control<number[]>([])
      })
    });

    // 1) Carichiamo le categorie dal backend
    this.metadataService.getCategories().subscribe({
      next: (cats) => {
        this.categories = cats; 
        // Se vuoi scegliere di default la prima categoria, potresti fare:
        // this.adForm.get('general.category')!.setValue(this.categories[0]);
      },
      error: (err) => console.error('Errore caricamento categories:', err)
    });

    // 2) Carichiamo i servizi dal backend
    this.metadataService.getServices().subscribe({
      next: (svcs) => {
        this.servicesList = svcs;
      },
      error: (err) => console.error('Errore caricamento services:', err)
    });
  }

  ngAfterViewInit(): void {
    // Inizializziamo Leaflet con coordinate di default
    this.initLeafletMap(45.4642, 9.1900);

    // inizializza mappa di recap solo quando il DOM è pronto
    setTimeout(() => {
      this.initRecapMap(
        this.adForm.get('address.locationCoords.lat')!.value || 45.4642,
        this.adForm.get('address.locationCoords.lng')!.value || 9.1900
      );
    });
  }

  private initLeafletMap(lat: number, lng: number) {
    if (this.map) {
      this.map.setView([lat, lng], 15);
      if (this.marker) {
        this.marker.setLatLng([lat, lng]);
      } else {
        this.marker = L.marker([lat, lng]).addTo(this.map);
      }
      return;
    }

    this.map = L.map(this.mapEl.nativeElement).setView([lat, lng], 15);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(this.map);
    this.marker = L.marker([lat, lng]).addTo(this.map);
  }

  onAddressInput() {
    const text = this.adForm.get('address.addressText')!.value;
    if (text && text.length > 2) {
      this.locationService.search(text).subscribe({
        next: (suggestions) => (this.addressSuggestions = suggestions),
        error: (err) => {
          console.error('Errore location.search:', err);
          this.addressSuggestions = [];
        }
      });
    } else {
      this.addressSuggestions = [];
    }
  }

  selectAddress(suggestion: string) {
    this.adForm.get('address.addressText')!.setValue(suggestion);
    this.addressSuggestions = [];

    this.locationService.getCoordsFromAddress(suggestion).subscribe({
      next: (coords: Coords) => {
        this.adForm.get('address.locationCoords.lat')!.setValue(coords.lat);
        this.adForm.get('address.locationCoords.lng')!.setValue(coords.lng);
        this.initLeafletMap(coords.lat, coords.lng);
      },
      error: (err) => console.error('Errore getCoordsFromAddress:', err)
    });
  }

  // Gestione upload file (come prima)
  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files) { return; }
    for (let i = 0; i < input.files.length; i++) {
      this.uploadedFiles.push(input.files[i]);
    }
    this.adForm.get('general.photos')!.setValue(this.uploadedFiles);
  }

  removeFile(index: number) {
    this.uploadedFiles.splice(index, 1);
    this.adForm.get('general.photos')!.setValue(this.uploadedFiles);
  }

  /**
   * Al click su una checkbox di un servizio, aggiorna l'array di ID
   */
  onServiceToggle(serviceId: number, checked: boolean) {
    const servicesArray = this.adForm.get('details.services')!.value as number[];
    if (checked) {
      // aggiungo l'ID nella lista
      this.adForm.get('details.services')!.setValue([...servicesArray, serviceId]);
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
    console.log('Dati annuncio da inviare:', payload);
    // this.adService.createAd(payload).subscribe(...)
  }

    // ————— Carousel helpers —————
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

  private initRecapMap(lat: number, lng: number) {
    if (this.recapMap) {
      this.recapMap.setView([lat, lng], 13);
      return;
    }
    this.recapMap = L.map(this.recapMapEl.nativeElement).setView([lat, lng], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution:
        '&copy; <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
    }).addTo(this.recapMap);
    L.marker([lat, lng]).addTo(this.recapMap);
  }

  // ————— Submit finale —————
  onPublish() {
    const payload = this.adForm.value;
    // chiama il tuo service per salvare l'annuncio:
    // this.adService.createAd(payload).subscribe(...)
    console.log('Publishing AD:', payload);
  }

  // Getter per le formGroup
  get generalGroup(): FormGroup {
    return this.adForm.get('general') as FormGroup;
  }
  get addressGroup(): FormGroup {
    return this.adForm.get('address') as FormGroup;
  }
  get detailsGroup(): FormGroup {
    return this.adForm.get('details') as FormGroup;
  }
}