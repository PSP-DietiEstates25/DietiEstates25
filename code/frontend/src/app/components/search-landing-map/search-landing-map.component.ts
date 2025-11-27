import {
  Component,
  inject,
  OnInit,
  AfterViewInit,
  OnDestroy,
  ViewChild,
  ElementRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import * as L from 'leaflet'; // Importa Leaflet

import { SearchFacade } from '../search/search.facade'; // Verifica path
import { GeoapifyService } from '../../manual_services/geoapify.service';
import { filter, lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-search-landing-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './search-landing-map.component.html',
  styleUrls: ['./search-landing-map.component.scss'], // Assicurati che il file scss esista anche se vuoto
})
export class SearchLandingMapComponent
  implements OnInit, AfterViewInit, OnDestroy
{
  private facade = inject(SearchFacade);
  private geoapifyService = inject(GeoapifyService);
  private router = inject(Router);

  @ViewChild('mapContainer') mapContainer!: ElementRef;
  private map: L.Map | undefined;

  // Layer Groups
  private boundariesLayer: L.LayerGroup = L.layerGroup();
  private markersLayer: L.LayerGroup = L.layerGroup();

  loading = true;
  infoMessage = 'Inizializzazione mappa...';

  cityName = '';
  regionName = '';

  ngOnInit(): void {
    const cachedGeo = (this.facade as any)._getCachedGeographicalPosition();
    this.cityName = cachedGeo.city;
    this.regionName = cachedGeo.state;
  }

  ngAfterViewInit(): void {
    this.map = L.map(this.mapContainer.nativeElement).setView(
      [41.9028, 12.4964],
      6,
    ); //centrato su italia

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);

    this.boundariesLayer.addTo(this.map);
    this.markersLayer.addTo(this.map);
    this.loadBoundaries();
  }

  async loadBoundaries() {
    this.loading = true;
    this.infoMessage = `Cerco i confini per ${this.cityName}...`;
    try {
      const placeId = await lastValueFrom(
        this.geoapifyService.getPlaceIdByCityAndRegion(
          this.cityName,
          this.regionName,
        ),
      );
      const subdivisions = await lastValueFrom(
        this.geoapifyService.getCityMunicipality(placeId),
      );
      const features = subdivisions?.features || [];
      const districts = this.filterPolygonalFeatures(features);

      await this.choosePolygonsDrawing(districts, placeId);
    } catch (err) {
      console.error(err);
      this.infoMessage = 'Errore nel caricamento della mappa. Riprova.';
    } finally {
      this.loading = false;
    }
  }

  async choosePolygonsDrawing(districts: any[], placeId: string) {
    if (districts.length > 0) {
      this.infoMessage = `Trovate ${districts.length} zone. Clicca su una zona per vedere gli annunci.`;
      this.drawPolygons(districts, true);
    } else {
      this.infoMessage = `Mostro annunci per tutta la città di ${this.cityName}.`;
      const cityDetails = await lastValueFrom(
        this.geoapifyService.getPlaceDetailsGeometry(placeId),
      );
      const cityFeature = cityDetails.features?.[0];

      if (cityFeature) {
        this.drawPolygons([cityFeature], false); // Disegna confine non cliccabile
      }

      // Carica subito gli annunci (municipalità null)
      this.performSearch(null);
    }
  }

  drawPolygons(features: any[], interactive: boolean) {
    if (!this.map) return;
    this.boundariesLayer.clearLayers();

    const geoJson = L.geoJSON(features as any, {
      style: {
        color: '#2563eb', // Blue-600
        weight: 6,
        opacity: 0.7,
        fillColor: '#3b82f6',
        fillOpacity: 0.1,
      },
      onEachFeature: (feature, layer) => {
        if (interactive) {
          // Eventi Mouse
          layer.on('mouseover', () =>
            (layer as L.Path).setStyle({ weight: 4, fillOpacity: 0.3 }),
          );
          layer.on('mouseout', () =>
            (layer as L.Path).setStyle({ weight: 2, fillOpacity: 0.1 }),
          );

          // Click sulla zona
          layer.on('click', () => {
            // Nome zona spesso in 'name' o 'name:it'
            const zoneName =
              feature.properties.name ||
              feature.properties['name:it'] ||
              'Zona selezionata';
            this.handleZoneClick(zoneName, layer);
          });

          if (feature.properties.name) {
            layer.bindTooltip(feature.properties.name, {
              sticky: true,
              direction: 'center',
            });
          }
        }
      },
    });

    this.boundariesLayer.addLayer(geoJson);

    // Zoomma per vedere tutti i poligoni
    if (geoJson.getBounds().isValid()) {
      this.map.fitBounds(geoJson.getBounds());
    }
  }

  filterPolygonalFeatures(features: any[]) {
    return features.filter(
      (feature: any) =>
        feature.geometry.type === 'Polygon' ||
        feature.geometry.type === 'MultiPolygon',
    );
  }

  handleZoneClick(zoneName: string, layer: any) {
    // Evidenzia visivamente la selezione (resetta gli altri)
    this.boundariesLayer.eachLayer((l: any) => {
      // Reset stile
      //this.boundariesLayer.(l);
    });
    // Applica stile attivo
    (layer as L.Path).setStyle({
      color: '#dc2626',
      fillColor: '#ef4444',
      fillOpacity: 0.3,
    }); // Rosso

    this.infoMessage = `Caricamento annunci a: ${zoneName}...`;
    this.performSearch(zoneName);
  }

  performSearch(municipality: string | null) {
    this.markersLayer.clearLayers();

    // Recupera i filtri salvati

    const geo = (this.facade as any)._getCachedGeographicalPosition();
    const util = (this.facade as any)._getCachedUtility();
    const cad = (this.facade as any)._getCachedCadastralFilter();

    // Aggiorna la municipalità nella richiesta
    // Se municipality è null (caso città intera), passiamo stringa vuota o null al backend

    const updatedGeo = { ...geo, municipality: municipality || '' };

    this.facade.geographicalPositionId.set(null);
    this.facade.detailId.set(null);

    this.facade
      .runFullSearch({
        category: 'SALE', // O parametrizzato
        page: 1,
        size: 100, // Prendiamo un po' di risultati per popolare la mappa
        geographicalPosition: updatedGeo,
        utility: util,
        cadastralFilter: cad,
      })
      .subscribe({
        next: () => {
          const cards = this.facade.searchCards();
          this.infoMessage = `Trovati ${cards.length} immobili a ${municipality || this.cityName}.`;
          this.addMarkers(cards);
        },
        error: () => {
          this.infoMessage = 'Nessun immobile trovato in questa zona.';
        },
      });
  }

  addMarkers(cards: any[]) {
    // Icona personalizzata (opzionale)
    const defaultIcon = L.icon({
      iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
      iconRetinaUrl:
        'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png',
      shadowUrl:
        'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
    });

    cards.forEach((card) => {
      // Assicuriamoci che abbia coordinate
      if (card.lat && card.lon) {
        const marker = L.marker([card.lat, card.lon], { icon: defaultIcon });
        marker.bindPopup(`
          <div style="font-family: sans-serif; font-size: 14px;">
            <strong>${card.title}</strong><br>
            Prezzo: €${card.price || 'N/D'}<br>
            <a href="/ad/${card.id}" style="color: blue; text-decoration: underline;">Vedi dettagli</a>
          </div>
        `);
        this.markersLayer.addLayer(marker);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove(); // Pulisci la mappa per evitare memory leak
    }
  }
}
