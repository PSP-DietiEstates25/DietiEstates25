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
  effect,
} from '@angular/core';

import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import * as L from 'leaflet';
import { SearchFacade } from '../search/search.facade';
import { GeoapifyService } from '../../manual_services/geoapify/geoapify.service';
import { lastValueFrom } from 'rxjs';
import { Geometry, FeatureCollection } from 'geojson';
import { environmentMap } from '../../../environments/environment.map';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { AdCategory } from '../../enums/ad-category.enum';
import { environment } from '../../../environments/environment';
import { MapPopupComponent } from '../map-popup/map-popup.component';
import { PriceIconComponent } from '../../shared/icons/price-icon/price-icon.component';
import { RoomsIconComponent } from '../../shared/icons/rooms-icon/rooms-icon.component';
import { SquareMetersIconComponent } from '../../shared/icons/square-meters-icon/square-meters-icon.component';
import { FloorIconComponent } from '../../shared/icons/floor-icon/floor-icon.component';
import { EnergyClassIconComponent } from '../../shared/icons/energy-class-icon/energy-class-icon.component';

// --- Icone Locali (Assicurati che i file siano in src/assets/) ---
const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41],
});
L.Marker.prototype.options.icon = iconDefault;
// ----------------------------------------------------------------

export interface MunicipalityToSelect {
  name: string;
  isSelected: boolean;
}

@Component({
  selector: 'app-search-landing-map',
  standalone: true,
  imports: [
    RouterLink,
    NavbarComponent,
    PriceIconComponent,
    RoomsIconComponent,
    SquareMetersIconComponent,
    FloorIconComponent,
    EnergyClassIconComponent,
  ],
  templateUrl: './search-landing-map.component.html',
  styleUrls: ['./search-landing-map.component.scss'],
})
export class SearchLandingMapComponent
  implements OnInit, AfterViewInit, OnDestroy
{
  facade = inject(SearchFacade);
  private geoapifyService = inject(GeoapifyService);
  private http = inject(HttpClient);
  private ngZone = inject(NgZone);
  private changeDetector = inject(ChangeDetectorRef);
  private injector = inject(EnvironmentInjector);
  private appRef = inject(ApplicationRef);

  readonly placeholder = '/assets/placeholder.jpg';

  @ViewChild('mapContainer') mapContainer!: ElementRef;
  private map!: L.Map;
  private markerIcon!: L.Icon;

  private boundariesLayer: L.LayerGroup = L.layerGroup();
  private markersLayer: L.LayerGroup = L.layerGroup();
  private geojson!: L.GeoJSON<any, Geometry>;

  municipalitiesSelection: MunicipalityToSelect[] = [];
  preselectedMunicipality: string | null = null;
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

  constructor() {
    effect(() => {
      const cards = this.facade.searchCards();
      if (this.map && cards.length > 0 && !this.loading) {
        this.addMarkers(cards, true);
      }
    });
  }

  ngOnInit(): void {
    const cachedGeo = this.facade.getCachedGeographicalPosition();
    if (cachedGeo) {
      this.cityName = cachedGeo.city!;
      this.regionName = (cachedGeo as any).state!;
      this.preselectedMunicipality = cachedGeo.municipality || null;
    }
  }

  ngAfterViewInit(): void {
    this.setMarkerIcon();
    this.setMap();
    this.boundariesLayer.addTo(this.map);
    this.markersLayer.addTo(this.map);

    const currentCards = this.facade.searchCards();
    const isReplay = currentCards.length > 0;

    if (isReplay) {
      this.isSelectingMunicipalities = false;
      this.infoMessage = `Visualizzazione risultati salvati (${currentCards.length})`;
      this.addMarkers(currentCards, false);
      this.loadBoundaries(true);
    } else {
      this.loadBoundaries(false);
    }

    this.changeDetector.detectChanges();
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
      attribution: 'Powered by Geoapify | © OpenStreetMap',
    }).addTo(this.map);

    L.control.zoom({ position: 'bottomright' }).addTo(this.map);

    this.map.on('movestart zoomstart', () => {
      this.isMapMoving = true;
    });
    this.map.on('moveend zoomend', () => {
      this.isMapMoving = false;
    });
  }

  async loadBoundaries(isReplay: boolean = false) {
    if (!this.cityName) {
      this.loading = false;
      return;
    }

    this.loading = true;
    if (!isReplay)
      this.infoMessage = `Ricerca dei confini per ${this.cityName}...`;

    try {
      // 1. Cerchiamo l'ID forzando "type=city" per evitare la provincia
      const apiKey = 'd6ef1142975941368b3831ce8487681b';
      const searchUrl = `https://api.geoapify.com/v1/geocode/search?text=${encodeURIComponent(this.cityName)}&type=city&limit=1&apiKey=${apiKey}`;

      const searchResult: any = await lastValueFrom(this.http.get(searchUrl));

      let placeId = '';
      if (searchResult.features && searchResult.features.length > 0) {
        placeId = searchResult.features[0].properties.place_id;
        console.log(`[DEBUG] City ID trovato (type=city): ${placeId}`);
      } else {
        placeId = await lastValueFrom(
          this.geoapifyService.getPlaceIdByCityAndRegion(
            this.cityName,
            this.regionName,
          ),
        );
      }

      // 2. Otteniamo i confini
      let boundaryData: FeatureCollection = await lastValueFrom(
        this.geoapifyService.getCityMunicipality(placeId),
      );

      // 3. Fallback per città singole
      if (!boundaryData.features || boundaryData.features.length === 0) {
        boundaryData = await lastValueFrom(
          this.geoapifyService.getPlaceDetailsGeometry(placeId),
        );
      }

      const features = boundaryData.features || [];
      this.boundariesLayer.clearLayers();

      this.geojson = L.geoJson(boundaryData, {
        style: () => this.getDefaultStyle(),
        onEachFeature: this.onEachFeature,
      }).addTo(this.map);

      this.boundariesLayer.addLayer(this.geojson);

      if (!isReplay) {
        if (features.length > 0) {
          this.showSelections(features);
          this.map.fitBounds(this.geojson.getBounds());
        } else {
          this.infoMessage = 'Nessun confine trovato.';
        }
      }

      if (isReplay && this.preselectedMunicipality) {
        this.highlightSavedMunicipality(this.preselectedMunicipality);
      } else if (isReplay) {
        this.zoomToMarkers();
      }
    } catch (error) {
      console.error('Errore boundaries:', error);
      if (!isReplay) this.infoMessage = 'Errore nel caricamento della mappa.';
    } finally {
      this.loading = false;
      this.changeDetector.detectChanges();
    }
  }

  highlightSavedMunicipality(name: string) {
    if (!this.geojson) return;

    let targetLayer: any = null;
    const targetName = name.trim().toLowerCase();

    const cleanTarget = targetName.replace('municipalità', '').trim();

    this.geojson.eachLayer((layer: any) => {
      const props = layer.feature.properties;

      const possibleNames = [
        props.name,
        props['name:it'],
        props.official_name,
        props.ref,
      ]
        .filter((n) => !!n)
        .map((n) => n.toString().trim().toLowerCase());

      const isMatch = possibleNames.some((pName) => {
        const cleanPName = pName.replace('municipalità', '').trim();
        return (
          pName === targetName ||
          pName.includes(targetName) ||
          targetName.includes(pName) ||
          (cleanTarget.length > 0 && cleanPName === cleanTarget)
        );
      });

      if (isMatch) {
        this.selectedLayer = layer;
        if (typeof layer.setStyle === 'function')
          layer.setStyle(this.getSelectedStyle());
        if (typeof layer.bringToFront === 'function') layer.bringToFront();
        targetLayer = layer;
      } else {
        this.geojson.resetStyle(layer);
      }
    });

    if (targetLayer && targetLayer.getBounds) {
      this.map.fitBounds(targetLayer.getBounds(), {
        padding: [30, 30],
        animate: true,
      });
    } else {
      this.zoomToMarkers();
    }
  }

  addMarkers(cards: any[], shouldZoom: boolean = true) {
    this.markersLayer.clearLayers();

    const groups = new Map<string, any[]>();
    cards.forEach((card) => {
      const lat = card.geographicalPosition.latitude;
      const lon = card.geographicalPosition.longitude;
      const key = `${lat},${lon}`;
      if (!groups.has(key)) groups.set(key, []);
      groups.get(key)!.push(card);
    });

    groups.forEach((groupCards) => {
      const count = groupCards.length;
      const first = groupCards[0];
      const lat = first.geographicalPosition.latitude;
      const lon = first.geographicalPosition.longitude;

      const iconToUse =
        count > 1
          ? this.geoapifyService.getCounterIcon(count)
          : this.markerIcon;
      const marker = L.marker([lat, lon], { icon: iconToUse });

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
        { minWidth: 280, maxWidth: 280, className: 'de-leaflet-popup' },
      );

      this.markersLayer.addLayer(marker);
    });

    if (shouldZoom && !this.selectedLayer) this.zoomToMarkers();
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
    this.infoMessage = 'Seleziona una zona.';
    this.facade.searchCards.set([]);
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

  onMunicipalityChange(name: string) {
    this.selectMunicipality(name);
  }

  showSelections(features: any[]) {
    this.isSelectingMunicipalities = true;
    this.municipalitiesSelection = [];
    for (const feature of features) {
      const name =
        feature.properties?.name ||
        feature.properties?.city ||
        'Zona sconosciuta';
      this.municipalitiesSelection.push({ name: name, isSelected: false });
    }
    this.municipalitiesSelection.sort((a, b) =>
      a.name.localeCompare(b.name, undefined, { numeric: true }),
    );
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
          if (typeof layer.bringToFront === 'function') layer.bringToFront();
        } else {
          this.geojson.resetStyle(layer);
        }
      });
    }
    if (!name) {
      this.selectedLayer = null;
      this.markersLayer.clearLayers();
      this.infoMessage = `Seleziona una zona.`;
    }
    this.changeDetector.detectChanges();
  }

  highlightFeature = (mouseEvent: any) => {
    if (this.isMapMoving || !this.isSelectingMunicipalities) return;
    const layer = mouseEvent.target;
    if (layer !== this.selectedLayer) {
      layer.setStyle(this.getHoverStyle());
      if (typeof layer.bringToFront === 'function') layer.bringToFront();
    }
  };

  resetHighlight = (mouseEvent: any) => {
    if (this.isMapMoving || !this.isSelectingMunicipalities) return;
    const layer = mouseEvent.target;
    if (layer !== this.selectedLayer) {
      this.geojson.resetStyle(layer);
      if (
        this.selectedLayer &&
        typeof (this.selectedLayer as any).bringToFront === 'function'
      ) {
        (this.selectedLayer as any).bringToFront();
      }
    }
  };

  handleLayerClick = (mouseEvent: any, feature: any) => {
    if (!this.isSelectingMunicipalities) return;
    const zoneName =
      feature.properties.name || feature.properties['name:it'] || '';
    const clickedLayer = mouseEvent.target;
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
    const searchTarget = selected ? selected.name : this.cityName;
    if (searchTarget) {
      this.infoMessage = `Caricamento annunci a: ${searchTarget}...`;
      this.performSearch(selected ? selected.name : null);
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

  ngOnDestroy(): void {
    if (this.map) {
      this.map.off('movestart zoomstart');
      this.map.off('moveend zoomend');
      this.map.remove();
    }
    this.facade.searchCards.set([]);
  }
}
