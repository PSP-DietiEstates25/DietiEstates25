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
import { Router, TitleStrategy } from '@angular/router';
import * as L from 'leaflet'; // Importa Leaflet

import { SearchFacade } from '../search/search.facade'; // Verifica path
import { GeoapifyService } from '../../manual_services/geoapify.service';
import { lastValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Geometry } from 'geojson';

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
  private map!: L.Map;

  // Layer Groups
  private boundariesLayer: L.LayerGroup = L.layerGroup();
  private markersLayer: L.LayerGroup = L.layerGroup();
  private geojson!: L.GeoJSON<any, Geometry>;
  private selectedLayer: L.Path | null = null;

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

    L.control.zoom({ position: 'bottomright' }).addTo(this.map);

    this.map.attributionControl
      .setPrefix('')
      .addAttribution(
        'Powered by <a href="https://www.geoapify.com/" target="_blank">Geoapify</a> | © OpenStreetMap <a href="https://www.openstreetmap.org/copyright" target="_blank">contributors</a>',
      );

    L.tileLayer(
      `https://maps.geoapify.com/v1/tile/klokantech-basic/{z}/{x}/{y}@2x.png?apiKey=${environment.geoapifyAPIKey}`,
      {
        attribution:
          'Powered by <a href="https://www.geoapify.com/" target="_blank">Geoapify</a> | © OpenStreetMap <a href="https://www.openstreetmap.org/copyright" target="_blank">contributors</a>',
        maxZoom: 20,
      },
    ).addTo(this.map);

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

      const features: any[] = subdivisions.features;

      if (features.length == 0) {
        const response = await lastValueFrom(
          this.geoapifyService.getPlaceDetailsGeometry(placeId),
        );
        this.geojson = L.geoJson(response, {
          style: () => this.getDefaultStyle(),
          onEachFeature: this.onEachFeature,
        }).addTo(this.map);
      } else {
        this.geojson = L.geoJson(features, {
          style: () => this.getDefaultStyle(),
          onEachFeature: this.onEachFeature,
        }).addTo(this.map);
        //const districts = this.filterPolygonalFeatures(features);
        //await this.choosePolygonsDrawing(districts, placeId);
      }

      this.map.fitBounds(this.geojson.getBounds(), {
        padding: [50, 50],
        animate: true,
      });

      this.boundariesLayer.addLayer(this.geojson);
    } catch (err) {
      console.error(err);
      this.infoMessage = 'Errore nel caricamento della mappa. Riprova.';
    } finally {
      this.loading = false;
    }
  }

  highlightFeature = (mouseEvent: any) => {
    const layer = mouseEvent.target;
    if (layer !== this.selectedLayer) {
      layer.setStyle(this.getHoverStyle());
      layer.bringToFront();
    }
  };

  resetHighlight = (mouseEvent: any) => {
    const layer = mouseEvent.target;
    if (layer !== this.selectedLayer) {
      this.geojson.resetStyle(layer);
    }
  };

  handleLayerClick = (e: any) => {
    const clickedLayer = e.target;

    if (this.selectedLayer === clickedLayer) {
      this.geojson.resetStyle(clickedLayer);
      this.selectedLayer = null;

      this.infoMessage = `Mostro annunci per tutta la città di ${this.cityName}.`;
      this.performSearch(null);

      this.map.fitBounds(this.geojson.getBounds());
    } else {
      if (this.selectedLayer) {
        this.geojson.resetStyle(this.selectedLayer);
      }
      this.selectedLayer = clickedLayer;
      clickedLayer.setStyle(this.getSelectedStyle());
      clickedLayer.bringToFront();
      this.map.fitBounds(clickedLayer.getBounds());
      /*
      const zoneName =
        clickedLayer.feature.properties.name || 'Zona selezionata';
      this.handleZoneClick(zoneName, clickedLayer); // La tua funzione esistente per cercare
      */
    }
  };

  onEachFeature = (feature: any, layer: L.Layer) => {
    layer.on({
      mouseover: this.highlightFeature,
      mouseout: this.resetHighlight,
      click: this.handleLayerClick,
    });
  };

  getDefaultStyle() {
    return {
      fillColor: '',
      weight: 2,
      opacity: 1,
      color: '#094585',
      dashArray: '1',
      fillOpacity: 0,
    };
  }

  getHoverStyle() {
    return {
      weight: 2,
      fillColor: '#5ea8f7',
      dashArray: '',
      fillOpacity: 0.2,
    };
  }

  getSelectedStyle() {
    return {
      weight: 2,
      fillColor: '#5ea8f7',
      dashArray: '',
      fillOpacity: 0.2,
    };
  }

  //----------------VECCHI--------------------
  async choosePolygonsDrawing(districts: any[], placeId: string) {
    if (districts.length > 0) {
      this.infoMessage = `Trovate ${districts.length} zone. Clicca su una zona per vedere gli annunci.`;
      this.drawDistricts(districts, true);
    } else {
      this.infoMessage = `Mostro annunci per tutta la città di ${this.cityName}.`;
      const cityDetails = await lastValueFrom(
        this.geoapifyService.getPlaceDetailsGeometry(placeId),
      );
      const cityFeature = cityDetails.features?.[0];

      if (cityFeature) {
        this.drawDistricts([cityFeature], false);
      }

      this.performSearch(null);
    }
  }

  drawDistricts(features: any[], interactive: boolean) {
    if (!this.map) return;
    this.boundariesLayer.clearLayers();

    const geoJson = L.geoJSON(features as any, {
      style: {
        color: '#2563eb', // Blue-600
        weight: 6,
        opacity: 0.7,
        fillColor: '#094585',
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
    this.boundariesLayer.eachLayer((l: any) => {
      // Reset stile
      //this.boundariesLayer(l);
    });
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

    const geo = (this.facade as any)._getCachedGeographicalPosition();
    const util = (this.facade as any)._getCachedUtility();
    const cad = (this.facade as any)._getCachedCadastralFilter();

    const updatedGeo = { ...geo, municipality: municipality || '' };

    this.facade.geographicalPositionId.set(null);
    this.facade.detailId.set(null);

    this.facade
      .runFullSearch({
        category: 'SALE',
        page: 1,
        size: 100,
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
