//create your environment.development.ts
export const environment = {
  production: true,
  apiBaseUrl: 'http://localhost:8080',
  apiKeyParam: 'apiKey=',
  geoapifyAPIKey: 'd6ef1142975941368b3831ce8487681b',
  placesRadius: 500,
  placesLimit: 20,
  loginUrl: `http://localhost:8080/oauth2/authorization/messaging-client-oidc?prompt=login`,
  searchFooterMaxVisiblePages: 5,
  offerFooterMaxVisiblePages: 5,
};
