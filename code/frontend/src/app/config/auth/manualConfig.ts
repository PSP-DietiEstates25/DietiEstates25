import { AuthConfig } from "angular-oauth2-oidc";
import { environment } from "../../../environments/environment";

export const manualConfig: AuthConfig = {
    issuer: environment.manualIssuer,
    redirectUri: environment.manualRedirectUri,
    clientId: environment.manualClientId,
    responseType: environment.manualResponseType,
    scope: environment.manualScope,
    requireHttps: environment.manualRequireHttps
}