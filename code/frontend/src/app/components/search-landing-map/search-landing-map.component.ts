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
  EnvironmentInjector,
  ApplicationRef,
  createComponent,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpBackend, HttpClient } from '@angular/common/http';
import * as L from 'leaflet';
import { SearchFacade } from '../search/search.facade';
import { GeoapifyService } from '../../manual_services/geoapify/geoapify.service';
import { lastValueFrom } from 'rxjs';
import { Geometry } from 'geojson';
import { environmentMap } from '../../../environments/environment.map';
import { ToastrService } from 'ngx-toastr';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { AdCategory } from '../../enums/ad-category.enum';
import { environment } from '../../../environments/environment';
// Importa il nuovo componente
import { MapPopupComponent } from '../map-popup/map-popup.component'; // Verifica il percorso

export interface MunicipalityToSelect {
  name: string;
  isSelected: boolean;
}

@Component({
  selector: 'app-search-landing-map',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './search-landing-map.component.html',
  styleUrls: ['./search-landing-map.component.scss'],
})
export class SearchLandingMapComponent
  implements OnInit, AfterViewInit, OnDestroy
{
  facade = inject(SearchFacade);
  private geoapifyService = inject(GeoapifyService);
  private ngZone = inject(NgZone);
  private changeDetector = inject(ChangeDetectorRef);
  private router = inject(Router);
  private toastrService = inject(ToastrService);

  // Injections per Componente Dinamico
  private injector = inject(EnvironmentInjector);
  private appRef = inject(ApplicationRef);

  private httpBackend = inject(HttpBackend);
  readonly placeholder = '/assets/placeholder.jpg';

  @ViewChild('mapContainer') mapContainer!: ElementRef;
  private map!: L.Map;
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
  query = '';

  private isMapMoving = false;
  private myRenderer = L.canvas({ padding: 0.5 });

  ngOnInit(): void {
    const cachedGeo = this.facade.getCachedGeographicalPosition()!;
    this.cityName = cachedGeo.city!;
    this.regionName = (cachedGeo as any).state!;
  }

  ngAfterViewInit(): void {
    this.setMarkerIcon();
    this.setMap();
    this.boundariesLayer.addTo(this.map);
    this.markersLayer.addTo(this.map);
    this.loadBoundaries();
  }

  getImageUrl(path?: string) {
    return `${environment.apiBaseUrl}${path}`;
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
      zoom: 11,
      renderer: this.myRenderer,
      zoomAnimation: true,
      fadeAnimation: true,
      markerZoomAnimation: true,
    });

    L.tileLayer(environmentMap.map_klokantech_basic, {
      attribution:
        'Powered by <a href="https://www.geoapify.com/" target="_blank">Geoapify</a> | © OpenStreetMap',
    }).addTo(this.map);

    L.control.zoom({ position: 'bottomright' }).addTo(this.map);

    this.map.on('movestart zoomstart', () => {
      this.isMapMoving = true;
    });
    this.map.on('moveend zoomend', () => {
      this.isMapMoving = false;
    });
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

      const polygonBounds = this.geojson.getBounds();
      const fitZoom = this.map.getBoundsZoom(polygonBounds, false);

      this.map.setView(polygonBounds.getCenter(), fitZoom, {
        animate: true,
        duration: 1,
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

  showSelections(features: any, hasMunicipalities: boolean) {
    this.isSelectingMunicipalities = true;
    if (hasMunicipalities) this.showMunicipalitiesSelection(features);
    else this.showCitySelection(features);
  }

  showMunicipalitiesSelection(features: any) {
    for (const feature of features) {
      this.municipalitiesSelection.push({
        name: feature.properties.name,
        isSelected: false,
      });
    }
    this.municipalitiesSelection.sort((a, b) =>
      a.name.localeCompare(b.name, undefined, { numeric: true }),
    );
  }

  showCitySelection(response: any) {
    this.municipalitiesSelection.push({
      name: response.features[0].properties.name,
      isSelected: false,
    });
  }

  selectMunicipality(name: string | null) {
    this.selectedMunicipality = name || '';
    this.municipalitiesSelection.forEach(
      (m) => (m.isSelected = m.name === name),
    );

    if (this.geojson) {
      this.geojson.eachLayer((layer: any) => {
        const featureName =
          layer.feature.properties.name || layer.feature.properties['name:it'];
        if (featureName === name) {
          this.selectedLayer = layer;
          layer.setStyle(this.getSelectedStyle());
          layer.bringToFront();
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

  // Eventi Mappa
  highlightFeature = (mouseEvent: any) => {
    if (this.isMapMoving || !this.isSelectingMunicipalities) return;
    const layer = mouseEvent.target;
    if (layer !== this.selectedLayer) {
      layer.setStyle(this.getHoverStyle());
      layer.bringToFront();
    }
  };

  resetHighlight = (mouseEvent: any) => {
    if (this.isMapMoving || !this.isSelectingMunicipalities) return;
    const layer = mouseEvent.target;
    if (layer !== this.selectedLayer) {
      this.geojson.resetStyle(layer);
      if (this.selectedLayer) (this.selectedLayer as any).bringToFront();
    }
  };

  handleLayerClick = (mouseEvent: any, feature: any) => {
    if (!this.isSelectingMunicipalities) return;
    const clickedLayer = mouseEvent.target;
    const zoneName =
      feature.properties.name || feature.properties['name:it'] || '';
    if (this.selectedLayer === clickedLayer) this.selectMunicipality(null);
    else this.selectMunicipality(zoneName);
  };

  onEachFeature = (feature: any, layer: L.Layer) => {
    layer.on({
      mouseover: this.highlightFeature,
      mouseout: this.resetHighlight,
      click: (e: L.LeafletMouseEvent) =>
        this.ngZone.run(() => this.handleLayerClick(e, feature)),
      dblclick: (e) => L.DomEvent.stopPropagation(e),
    });
  };

  getDefaultStyle() {
    return {
      fillColor: '#FFFFFF',
      weight: 1,
      opacity: 1,
      color: '#094585',
      fillOpacity: 0,
    };
  }
  getHoverStyle() {
    return {
      weight: 2,
      color: '#094585',
      fillColor: '#5ea8f7',
      dashArray: '',
      fillOpacity: 0.2,
    };
  }
  getSelectedStyle() {
    return {
      weight: 3,
      color: '#094585',
      fillColor: '#5ea8f7',
      dashArray: '',
      fillOpacity: 0.2,
    };
  }

  onSearch() {
    this.isSelectingMunicipalities = false;
    const selected = this.municipalitiesSelection.find((m) => m.isSelected);
    if (selected) {
      this.infoMessage = `Caricamento annunci a: ${selected.name}...`;
      this.performSearch(selected.name);
    }
  }

  performSearch(municipality: string | null) {
    this.markersLayer.clearLayers();
    const geoPos = (this.facade as any).getCachedGeographicalPosition();
    const utility = (this.facade as any).getCachedUtility();
    const cadastral = (this.facade as any).getCachedCadastralFilter();

    this.facade.geographicalPositionId.set(null);
    this.facade.detailId.set(null);

    this.facade
      .runFullSearch({
        category: AdCategory.Sale,
        geographicalPosition: { ...geoPos, municipality: municipality || '' },
        utility: utility,
        cadastralFilter: cadastral,
      })
      .subscribe({
        next: () => {
          const cards = this.facade.searchCards();
          this.infoMessage = `Trovati ${cards.length} immobili a ${municipality || this.cityName}.`;
          this.addMarkers(cards);
          this.changeDetector.detectChanges();
        },
        error: () => {
          this.infoMessage = 'Nessun immobile trovato in questa zona.';
          this.changeDetector.detectChanges();
        },
      });
  }

  // =======================================================================
  //  NUOVA LOGICA PER MARKER E POPUP DINAMICI
  // =======================================================================

  addMarkers(cards: any[]) {
    this.markersLayer.clearLayers();

    // 1. Raggruppa gli immobili con le stesse coordinate
    const groups = new Map<string, any[]>();
    cards.forEach((card) => {
      const latitude = card.geographicalPosition.latitude;
      const longitude = card.geographicalPosition.longitude;
      const key = `${latitude},${longitude}`;
      if (!groups.has(key)) groups.set(key, []);
      groups.get(key)!.push(card);
    });

    groups.forEach((groupCards, key) => {
      const count = groupCards.length;
      const first = groupCards[0];
      const latitude = first.geographicalPosition.latitude;
      const longitude = first.geographicalPosition.longitude;

      const iconToUse =
        count > 1 ? this.getGeoapifyCounterIcon(count) : this.markerIcon;

      const marker = L.marker([latitude, longitude], { icon: iconToUse });

      marker.bindPopup(
        () => {
          const componentRef = createComponent(MapPopupComponent, {
            environmentInjector: this.injector,
          });

          componentRef.setInput('cards', groupCards);

          this.appRef.attachView(componentRef.hostView);

          const domElem = (componentRef.hostView as any)
            .rootNodes[0] as HTMLElement;

          domElem.addEventListener(
            'remove',
            () => {
              this.appRef.detachView(componentRef.hostView);
              componentRef.destroy();
            },
            { once: true },
          );

          componentRef.changeDetectorRef.detectChanges();

          return domElem;
        },
        {
          minWidth: 280,
          maxWidth: 280,
          className: 'de-leaflet-popup',
        },
      );

      this.markersLayer.addLayer(marker);
    });

    if (this.selectedLayer && (this.selectedLayer as any).getBounds) {
      this.map.fitBounds((this.selectedLayer as any).getBounds(), {
        animate: true,
        duration: 0.8,
      });
    } else {
      this.zoomToMarkers();
    }
  }

  getGeoapifyCounterIcon(count: number): L.Icon {
    const apiKey = environment.geoapifyAPIKey || '';

    const params = new URLSearchParams({
      apiKey: apiKey,
      type: 'material',
      color: '#094585',
      text: count.toString(),
      textColor: '#ffffff',
      textSize: 'small',
      scaleFactor: '2',
    });

    return L.icon({
      iconUrl: `https://api.geoapify.com/v1/icon?${params.toString()}`,
      iconSize: [31, 46],
      iconAnchor: [15.5, 46],
      popupAnchor: [0, -46],
      // shadowUrl: environmentMap.map_house_marker_shadow
    });
  }

  filteredMunicipalities(): MunicipalityToSelect[] {
    const q = (this.query || '').trim().toLowerCase();
    if (!q) return this.municipalitiesSelection;
    return this.municipalitiesSelection.filter((m) =>
      (m.name || '').toLowerCase().includes(q),
    );
  }

  resetToMunicipalities() {
    this.isSelectingMunicipalities = true;
    this.selectedMunicipality = '';
    this.query = '';
    this.markersLayer.clearLayers();
    if (this.geojson) this.geojson.eachLayer((l) => this.geojson.resetStyle(l));
    this.selectedLayer = null;
    this.infoMessage = 'Seleziona una municipalità.';
    this.changeDetector.detectChanges();
  }

  zoomToMarkers() {
    const latlngs: L.LatLng[] = [];
    this.markersLayer.eachLayer((layer: any) => {
      if (layer.getLatLng) latlngs.push(layer.getLatLng());
    });
    if (latlngs.length)
      this.map.fitBounds(L.latLngBounds(latlngs), {
        padding: [40, 40],
        animate: true,
      });
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.off('movestart zoomstart');
      this.map.off('moveend zoomend');
      this.map.remove();
    }
  }
}
