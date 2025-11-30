import { environment } from './environment';

//MapLibre GL Map styles
export const environmentMap = {
  production: true,
  apiKeyParameter: 'apiKey=',
  map_osm_bright:
    'https://maps.geoapify.com/v1/tile/osm-bright/{z}/{x}/{y}@2x.png?' +
    environment.apiKeyParam +
    environment.geoapifyAPIKey,
  map_klokantech_basic:
    'https://maps.geoapify.com/v1/tile/klokantech-basic/{z}/{x}/{y}@2x.png?' +
    environment.apiKeyParam +
    environment.geoapifyAPIKey,
  map_house_marker_shadow:
    'https://api.geoapify.com/v2/icon/?type=material&color=%230070e2&size=42&icon=house&iconType=awesome&contentSize=15&scaleFactor=2&' +
    environment.apiKeyParam +
    environment.geoapifyAPIKey,
  initialLatitude: 40.8358846,
  initialLongitude: 14.2487679,
  initialZoom: 10.5,
  initialCanvasContextAttribute: { preserveDrawingBuffer: true },
};
