import {
  Component,
  inject,
  OnInit,
  AfterViewInit,
  OnDestroy,
  ViewChild,
  ElementRef,
  NgZone,
  ChangeDetectorRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, TitleStrategy } from '@angular/router';
import * as L from 'leaflet'; // Importa Leaflet

import { SearchFacade } from '../search/search.facade'; // Verifica path
import { GeoapifyService } from '../../manual_services/geoapify.service';
import { lastValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Geometry } from 'geojson';
import { environmentMap } from '../../../environments/environment.map';
import { RouterLink } from '@angular/router';

export interface MunicipalityToSelect {
  name: string;
  isSelected: boolean;
}

@Component({
  selector: 'app-search-landing-map',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './search-landing-map.component.html',
  styleUrls: ['./search-landing-map.component.scss'], // Assicurati che il file scss esista anche se vuoto
})
export class SearchLandingMapComponent
  implements OnInit, AfterViewInit, OnDestroy
{
  facade = inject(SearchFacade);
  private geoapifyService = inject(GeoapifyService);
  private ngZone = inject(NgZone);
  private changeDetector = inject(ChangeDetectorRef);
  private router = inject(Router);

  @ViewChild('mapContainer') mapContainer!: ElementRef;
  private map!: L.Map;
  private marker!: L.Marker;
  private markerIcon!: L.Icon;

  // Layer Groups
  private boundariesLayer: L.LayerGroup = L.layerGroup();
  private markersLayer: L.LayerGroup = L.layerGroup();
  private geojson!: L.GeoJSON<any, Geometry>;
  municipalitiesSelection: MunicipalityToSelect[] = [];
  selectedLayer: L.Path | null = null;
  isSelectingMunicipalities = false;

  loading = true;
  infoMessage = 'Inizializzazione mappa...';
  selectedMunicipality!: string;

  cityName = '';
  regionName = '';

  ngOnInit(): void {
    const cachedGeo = (this.facade as any)._getCachedGeographicalPosition();
    this.cityName = cachedGeo.city;
    this.regionName = cachedGeo.state;
  }

  ngAfterViewInit(): void {
    this.setMarkerIcon();
    this.setMap();
    this.boundariesLayer.addTo(this.map);
    this.markersLayer.addTo(this.map);

    this.loadBoundaries();
  }

  setMarkerIcon() {
    this.markerIcon = L.icon({
      iconUrl: environmentMap.map_house_marker_shadow,
      iconSize: [31, 46],
      iconAnchor: [15.5, 46],
      popupAnchor: [0, -46],
    });
  }

  setMap() {
    this.map = new L.Map(this.mapContainer.nativeElement, {
      center: [41.9028, 12.4964],
      zoom: 6,
      doubleClickZoom: false,
    }); //centrato su italia

    L.tileLayer(environmentMap.map_klokantech_basic, {
      attribution:
        'Powered by <a href="https://www.geoapify.com/" target="_blank">Geoapify</a> | © OpenStreetMap <a href="https://www.openstreetmap.org/copyright" target="_blank">contributors</a>',
      maxZoom: 20,
    }).addTo(this.map);

    L.control.zoom({ position: 'bottomright' }).addTo(this.map);
  }

  async loadBoundaries() {
    this.loading = true;
    this.infoMessage = `Ricerca dei confini per ${this.cityName}...`;
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
        this.showSelections(response, false);
      } else {
        this.geojson = L.geoJson(features, {
          style: () => this.getDefaultStyle(),
          onEachFeature: this.onEachFeature,
        }).addTo(this.map);
        this.showSelections(features, true);
      }

      this.map.fitBounds(this.geojson.getBounds(), {
        padding: [50, 50],
        animate: true,
      });

      this.boundariesLayer.addLayer(this.geojson);
    } catch (error) {
      this.infoMessage = 'Errore nel caricamento della mappa. Riprova.';
    } finally {
      this.loading = false;
    }
  }

  onMunicipalityChange(name: string) {
    this.selectMunicipality(name);
  }

  showSelections(features: any, hasMunicitpalities: boolean) {
    this.isSelectingMunicipalities = true;
    //se viene passata la città, viene mostrata solo la città intera da selezionare
    //altrimenti vengono messe tutte le municipalità
    if (hasMunicitpalities) this.showMunicipalitiesSelection(features);
    else this.showCitySelection(features);
  }

  showMunicipalitiesSelection(features: any) {
    for (const feature of features) {
      const municipalityToSelect: MunicipalityToSelect = {
        name: feature.properties.name,
        isSelected: false,
      };
      this.municipalitiesSelection.push(municipalityToSelect);
    }

    this.municipalitiesSelection.sort((a, b) => {
      return a.name.localeCompare(b.name, undefined, { numeric: true });
    });
  }

  showCitySelection(response: any) {
    const municipalityToSelect: MunicipalityToSelect = {
      name: response.features[0].properties.name,
      isSelected: false,
    };
    this.municipalitiesSelection.push(municipalityToSelect);
  }

  selectMunicipality(name: string | null) {
    this.selectedMunicipality = name || '';

    this.municipalitiesSelection.forEach((municipality) => {
      if (municipality.name === name) {
        municipality.isSelected = true;
      } else municipality.isSelected = false;
    });

    let foundLayer: any = null;

    if (this.geojson) {
      this.geojson.eachLayer((layer: any) => {
        const featureName =
          layer.feature.properties.name || layer.feature.properties['name:it'];

        if (featureName === name) {
          this.selectedLayer = layer;
          layer.setStyle(this.getSelectedStyle());
          layer.bringToFront();
          foundLayer = layer;
        } else {
          this.geojson.resetStyle(layer);
        }
      });
    }

    if (!name) {
      this.selectedLayer = null;
      this.markersLayer.clearLayers();
      this.infoMessage = `Seleziona una municipalità.`;
    }

    this.changeDetector.detectChanges();
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

  handleLayerClick = (mouseEvent: any, feature: any) => {
    const clickedLayer = mouseEvent.target;

    const zoneName =
      feature.properties.name || feature.properties['name:it'] || '';

    if (this.selectedLayer === clickedLayer) {
      this.selectMunicipality(null);
    } else {
      this.selectMunicipality(zoneName);
    }
  };

  onEachFeature = (feature: any, layer: L.Layer) => {
    layer.on({
      mouseover: this.highlightFeature,
      mouseout: this.resetHighlight,
      click: (mouseEvent: L.LeafletMouseEvent) => {
        this.ngZone.run(() => {
          this.handleLayerClick(mouseEvent, feature);
        });
      },
      dblclick: (mouseEvent: L.LeafletMouseEvent) => {
        L.DomEvent.stopPropagation(mouseEvent);
      },
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

  onSearch() {
    this.isSelectingMunicipalities = false;
    let selectedMunicipality: MunicipalityToSelect;
    this.municipalitiesSelection.forEach((municipality) => {
      if (municipality.isSelected) selectedMunicipality = municipality;
    });
    this.infoMessage = `Caricamento annunci a: ${selectedMunicipality!.name}...`;
    console.log(selectedMunicipality!);
    this.performSearch(selectedMunicipality!.name);
  }

  performSearch(municipality: string | null) {
    this.markersLayer.clearLayers();

    const geographicalPosition = (
      this.facade as any
    )._getCachedGeographicalPosition();
    const utility = (this.facade as any)._getCachedUtility();
    const cadastralFilter = (this.facade as any)._getCachedCadastralFilter();

    const updatedGeo = {
      ...geographicalPosition,
      municipality: municipality || '',
    };

    this.facade.geographicalPositionId.set(null);
    this.facade.detailId.set(null);

    this.facade
      .runFullSearch({
        category: 'SALE',
        page: 1,
        size: 100,
        geographicalPosition: updatedGeo,
        utility: utility,
        cadastralFilter: cadastralFilter,
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
    cards.forEach((card) => {
      if (card.lat && card.lon) {
        const marker = L.marker([card.lat, card.lon], {
          icon: this.markerIcon,
        });
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
      this.map.remove();
    }
  }
}
