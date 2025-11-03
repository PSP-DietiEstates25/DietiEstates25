import { environment } from "./environment"

//MapLibre GL Map styles
export const environmentMap = {
    production: true,
    apiKeyParameter: "apiKey=",
    map_osm_carto: "https://maps.geoapify.com/v1/styles/osm-carto/style.json?" + environment.apiKeyParam + environment.geoapifyAPIKey,
    map_osm_bright: "https://maps.geoapify.com/v1/styles/osm-bright/style.json?" + environment.apiKeyParam + environment.geoapifyAPIKey,
    map_osm_bright_gray: "https://maps.geoapify.com/v1/styles/osm-bright-grey/style.json?" + environment.apiKeyParam + environment.geoapifyAPIKey,
    map_osm_bright_smooth: "https://maps.geoapify.com/v1/styles/osm-bright-smooth/style.json?" + environment.apiKeyParam + environment.geoapifyAPIKey,
    map_klokantech_basic: "https://maps.geoapify.com/v1/styles/klokantech-basic/style.json?" + environment.apiKeyParam + environment.geoapifyAPIKey,
    map_house_marker_layer: "https://api.geoapify.com/v2/icon/?type=material&color=red&size=52&icon=house&contentSize=15&scaleFactor=2&" + environment.apiKeyParam + environment.geoapifyAPIKey,
    initialLatitude: 40.8358846,
    initialLongitude: 14.2487679,
    initialZoom: 10.5,
    initialCanvasContextAttribute: {preserveDrawingBuffer: true},
};