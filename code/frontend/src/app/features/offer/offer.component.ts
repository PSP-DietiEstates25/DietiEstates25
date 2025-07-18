import { Component } from '@angular/core';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { Router } from '@angular/router';
import { _MatInternalFormField } from '@angular/material/core';
import { DecimalPipe } from '@angular/common';

interface Appartamento {
  nome: string;
  mieOfferte: number[];
  controfferte: number[];
}

@Component({
  selector: 'app-offer',
  standalone: true,
  imports: [
    NavbarComponent,
    DecimalPipe
  ],
  templateUrl: './offer.component.html',
  styleUrl: './offer.component.scss'
})
export class OfferComponent {
  // Array di appartamenti con dati di esempio
  appartamenti: Appartamento[] = [
    {
      nome: 'Appartamento in centro',
      mieOfferte: [200000, 210000],
      controfferte: [215000, 212000],
    },
    {
      nome: 'Trilocale in periferia',
      mieOfferte: [150000],
      controfferte: [],
    },
    {
      nome: 'Bilocale zona mare',
      mieOfferte: [180000, 185000],
      controfferte: [190000],
    },
  ];

   //  Variabile privata interna
  private _selectedApartment: Appartamento | null = null;

  //  Getter: per leggere il valore
  get selectedApartment(): Appartamento | null {
    return this._selectedApartment;
  }

  //  Setter: per modificare il valore con logica
  set selectedApartment(value: Appartamento | null) {
    
    this._selectedApartment = value;
  }

  //  Metodo per selezionare o deselezionare una card
  toggleApartment(apt: Appartamento): void {
    this.selectedApartment =
      this._selectedApartment === apt ? null : apt;
  }

  //  Serve per sapere se un apt è selezionato
  isSelected(apt: Appartamento): boolean {
    return this.selectedApartment === apt;
  }

  //  Mostra messaggio se non ci sono controfferte
  hasCounteroffers(): boolean {
    return (
      this.selectedApartment?.controfferte?.length! > 0
    );
  }
}


/**
  selectedApartment: Appartamento | null = null;
  
  //Funzione che controlla se un appartamento è selezionato (utile in HTML per mostrare/nascondere i dettagli).
  isSelected(apt: Appartamento): boolean {
    return this.selectedApartment === apt;
  }

  //Funzione che verifica se un appartamento ha controfferte (array non vuoto).
  hasCounteroffers(apt: Appartamento): boolean {
    return apt.controfferte.length > 0;
  }

  //Funzione per selezionare/deselezionare un appartamento quando ci clicchi sopra
  toggleApartment(apt: Appartamento): void {
    if (this.isSelected(apt)) {
      // Se l'appartamento è già selezionato, lo deseleziona
      this.selectedApartment = null;
    } else {
      // Altrimenti seleziona il nuovo appartamento (automaticamente deselezionando il precedente)
      this.selectedApartment = apt;
    }
  }
     */
