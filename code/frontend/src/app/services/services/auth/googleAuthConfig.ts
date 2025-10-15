import { AuthConfig } from "angular-oauth2-oidc";
import { CompoundExpression } from "maplibre-gl";

export const googleAuthConfig: AuthConfig = {
    issuer: "http://localhost:9090",
    responseType: "code",
    redirectUri: "http://localhost:4200/search",
    //clientId: "817273458156-75m4gd9skara9hcbmfb9mthe8b33us20.apps.googleusercontent.com",
    clientId: "angularspa",
    //scope: "openid profile email",
    scope: "openid",
    strictDiscoveryDocumentValidation: false,
};