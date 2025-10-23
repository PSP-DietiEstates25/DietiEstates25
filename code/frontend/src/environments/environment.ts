//create your environment.development.ts
export const environment = {
    production: true,
    apiBaseUrl: 'http://localhost:8080',
    apiKeyParam: "apiKey=",
    geoapifyAPIKey: "secretKey",

    manualIssuer: "manualIssuer",
    manualRedirectUri: "manualRedirectUri",
    manualClientId: "manualClientId",
    manualResponseType: "manualResponseType",
    manualScope: "manualScope",
    manualRequireHttps: false,

    googleIssuer: "googleIssuer",
    googleRedirectUri: "googleRedirectUri",
    googleClientId: "googleClientId",
    googleResponseType: "googleResponseType",
    googleScope: "googleScope",
    googleRequireHttps: false,

    githubIssuer: "githubIssuer",
    githubRedirectUri: "githubRedirectUri",
    githubClientId: "githubClientId",
    githubResponseType: "githubResponseType",
    githubScope: "githubScope",
    githubRequireHttps: false,

    facebookIssuer: "facebookIssuer",
    facebookRedirectUri: "facebookRedirectUri",
    facebookClientId: "facebookClientId",
    facebookResponseType: "facebookResponseType",
    facebookScope: "facebookScope",
    facebookRequireHttps: false
};
