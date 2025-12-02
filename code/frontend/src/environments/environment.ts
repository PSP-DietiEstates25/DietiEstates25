//create your environment.development.ts
export const environment = {
  production: true,
  apiBaseUrl: 'http://localhost:8080',
  apiKeyParam: 'apiKey=',
  geoapifyAPIKey: 'secretKey',
  loginUrl: `http://localhost:8080/oauth2/authorization/messaging-client-oidc?prompt=login`,
  placesRadius: 500,
  placesLimit: 20,
  searchFooterMaxVisiblePages: 5,
  offerFooterMaxVisiblePages: 5,
};
