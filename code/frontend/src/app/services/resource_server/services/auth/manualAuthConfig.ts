import { AuthConfig } from 'angular-oauth2-oidc';

export const authCodeFlowConfig: AuthConfig = {
    issuer: "http://localhost:9090",
    redirectUri: window.location.origin + 'index.html',
    clientId: "angular-spa",
    responseType: "client",
    scope: "openid",
    showDebugInformation: true
}