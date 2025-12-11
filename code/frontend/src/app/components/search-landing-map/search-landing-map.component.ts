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

export interface MunicipalityToSelect {
  name: string;
  isSelected: boolean;
}

const isHttp = (s: string) => /^https?:\/\//i.test(s);
const isData = (s: string) => /^data:/i.test(s);
const looksJpeg = (b64: string) => b64?.startsWith('/9j/');
const looksPng = (b64: string) => b64?.startsWith('iVBOR');

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

  private httpBackend = inject(HttpBackend);
  private httpNoInter = new HttpClient(this.httpBackend);
  private blobCache = new Map<string, string>();
  private pending = new Set<string>();
  readonly placeholder = '/assets/placeholder.jpg';

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
  query = '';

  // FIX: Flag per bloccare eventi hover durante lo zoom/pan
  private isMapMoving = false;
  // FIX: Renderer Canvas per prestazioni migliori
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
      // FIX: Usare renderer Canvas invece di SVG (default) per evitare lag grafico
      renderer: this.myRenderer,
      zoomAnimation: true,
      fadeAnimation: true,
      markerZoomAnimation: true,
    });

    L.tileLayer(environmentMap.map_klokantech_basic, {
      attribution:
        'Powered by <a href="https://www.geoapify.com/" target="_blank">Geoapify</a> | © OpenStreetMap <a href="https://www.openstreetmap.org/copyright" target="_blank">contributors</a>',
    }).addTo(this.map);

    L.control.zoom({ position: 'bottomright' }).addTo(this.map);

    // FIX: Listener per gestire lo stato di movimento della mappa
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
      const polygonCenter = polygonBounds.getCenter();
      const fitZoom = this.map.getBoundsZoom(polygonBounds, false);

      this.map.setView(polygonCenter, fitZoom, {
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

  showSelections(features: any, hasMunicitpalities: boolean) {
    this.isSelectingMunicipalities = true;
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

      if (this.selectedLayer) {
        (this.selectedLayer as any).bringToFront();
      }
    }
  };

  handleLayerClick = (mouseEvent: any, feature: any) => {
    if (!this.isSelectingMunicipalities) return;
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
    ).getCachedGeographicalPosition();
    const utility = (this.facade as any).getCachedUtility();
    const cadastralFilter = (this.facade as any).getCachedCadastralFilter();

    const updatedGeo = {
      ...geographicalPosition,
      municipality: municipality || '',
    };

    this.facade.geographicalPositionId.set(null);
    this.facade.detailId.set(null);

    this.facade
      .runFullSearch({
        category: AdCategory.Sale,
        geographicalPosition: updatedGeo,
        utility: utility,
        cadastralFilter: cadastralFilter,
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

  private popupHtml(c: any) {
    const title = c?.title ?? 'Immobile';
    const addr =
      [c?.address, c?.city].filter(Boolean).join(', ') ||
      'Indirizzo non disponibile';

    return `
    <div class="de-popup">
      <div class="de-title">${this.escapeHtml(title)}</div>
      <div class="de-addr">${this.escapeHtml(addr)}</div>
      <button class="de-btn" data-id="${c?.id ?? ''}">Apri annuncio</button>
    </div>`;
  }

  onCancel() {
    this.router.navigate(['/searches']);
    this.toastrService.error('Ricerca interrotta');
  }

  addMarkers(cards: any[]) {
    this.markersLayer.clearLayers();

    console.log('Cards ricevute:', cards);

    for (const card of cards) {
      const latitude = card.geographicalPosition.latitude;
      const longitude = card.geographicalPosition.longitude;

      const marker = L.marker([latitude, longitude], { icon: this.markerIcon });

      marker.bindPopup(this.popupHtml(card), {
        className: 'de-leaflet-popup',
        minWidth: 260,
      });

      marker.on('popupopen', (e: any) => {
        const element = e.popup.getElement() as HTMLElement | null;
        const btn = element?.querySelector<HTMLButtonElement>('.de-btn');
        btn?.addEventListener('click', (ev) => {
          ev.preventDefault();
          ev.stopPropagation();
          const id = (btn.dataset['id'] ?? '').trim();
          if (!id) return;
          this.ngZone.run(() => this.router.navigate(['/ad', id]));
        });
      });

      this.markersLayer.addLayer(marker);
    }

    if (this.selectedLayer && (this.selectedLayer as any).getBounds) {
      const bounds = (this.selectedLayer as any).getBounds();

      this.map.fitBounds(bounds, {
        animate: true,
        duration: 0.8,
      });
    } else {
      this.zoomToMarkers();
    }
  }

  private escapeHtml(s: string) {
    return s
      .replaceAll('&', '&amp;')
      .replaceAll('<', '&lt;')
      .replaceAll('>', '&gt;')
      .replaceAll('"', '&quot;')
      .replaceAll("'", '&#039;');
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

    try {
      if (this.geojson) {
        this.geojson.eachLayer((layer: any) => {
          this.geojson.resetStyle(layer);
        });
      }
    } catch {}

    this.selectedLayer = null;
    this.infoMessage = 'Seleziona una municipalità.';
    this.changeDetector.detectChanges();
  }

  zoomToMarkers() {
    try {
      const latlngs: L.LatLng[] = [];

      for (const layer of this.markersLayer.getLayers()) {
        const m: any = layer;
        if (m?.getLatLng) latlngs.push(m.getLatLng());
      }

      if (!latlngs.length) return;

      const bounds = L.latLngBounds(latlngs);
      this.map.fitBounds(bounds, { padding: [40, 40], animate: true });
    } catch {}
  }

  ngOnDestroy(): void {
    try {
      if (this.map) {
        this.map.off('movestart zoomstart');
        this.map.off('moveend zoomend');
        this.map.remove();
      }
    } catch {}

    for (const url of this.blobCache.values()) URL.revokeObjectURL(url);
    this.blobCache.clear();
    this.pending.clear();
  }
}
