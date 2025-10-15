import { AuthConfig } from "angular-oauth2-oidc";

export const googleAuthConfig: AuthConfig = {
    issuer: "https://accounts.google.com",
    redirectUri: window.location.origin,
    clientId: "817273458156-75m4gd9skara9hcbmfb9mthe8b33us20.apps.googleusercontent.com",
    scope: "openid profile email",
    strictDiscoveryDocumentValidation: false,
};