package com.dietiestates.auth.repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.dietiestates.auth.model.Client;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper;

    public JpaRegisteredClientRepository(ClientRepository clientRepository, ObjectMapper objectMapper) {
        Assert.notNull(clientRepository, "clientRepository cannot be null");
        Assert.notNull(objectMapper, "objectMapper cannot be null");
        this.clientRepository = clientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        this.clientRepository.save(toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.clientRepository.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return this.clientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    private RegisteredClient toObject(Client client) {
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(client.getRedirectUris());
        Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(client.getPostLogoutRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(client.getScopes());

        RegisteredClient.Builder builder = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName())
                .clientAuthenticationMethods(m ->
                        clientAuthenticationMethods.forEach(am -> m.add(resolveClientAuthenticationMethod(am))))
                .authorizationGrantTypes(g ->
                        authorizationGrantTypes.forEach(gt -> g.add(resolveAuthorizationGrantType(gt))))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                .scopes(sc -> sc.addAll(clientScopes));

        // --- ClientSettings (normalizzati) ---
        Map<String, Object> clientSettingsMap = parseMap(client.getClientSettings());
        builder.clientSettings(buildClientSettings(clientSettingsMap));

        // --- TokenSettings (normalizzati) ---
        Map<String, Object> tokenSettingsMap = parseMap(client.getTokenSettings());
        builder.tokenSettings(buildTokenSettings(tokenSettingsMap));

        return builder.build();
    }

    private Client toEntity(RegisteredClient rc) {
        List<String> authMethods = new ArrayList<>(rc.getClientAuthenticationMethods().size());
        rc.getClientAuthenticationMethods().forEach(cam -> authMethods.add(cam.getValue()));

        List<String> grantTypes = new ArrayList<>(rc.getAuthorizationGrantTypes().size());
        rc.getAuthorizationGrantTypes().forEach(gt -> grantTypes.add(gt.getValue()));

        Client e = new Client();
        e.setId(rc.getId());
        e.setClientId(rc.getClientId());
        e.setClientIdIssuedAt(rc.getClientIdIssuedAt());
        e.setClientSecret(rc.getClientSecret());
        e.setClientSecretExpiresAt(rc.getClientSecretExpiresAt());
        e.setClientName(rc.getClientName());
        e.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(authMethods));
        e.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(grantTypes));
        e.setRedirectUris(StringUtils.collectionToCommaDelimitedString(rc.getRedirectUris()));
        e.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(rc.getPostLogoutRedirectUris()));
        e.setScopes(StringUtils.collectionToCommaDelimitedString(rc.getScopes()));
        e.setClientSettings(writeMap(rc.getClientSettings().getSettings()));
        e.setTokenSettings(writeMap(rc.getTokenSettings().getSettings()));
        return e;
    }

    // ----------------------------
    // (De)serializzazione robusta
    // ----------------------------
    private Map<String, Object> parseMap(String data) {
        try {
            if (!StringUtils.hasText(data)) return Map.of();
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data != null ? data : Map.of());
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    // ----------------------------
    // Normalizzazione settings
    // ----------------------------
    private ClientSettings buildClientSettings(Map<String, Object> raw) {
        Map<String, Object> fixed = new HashMap<>(raw);
        // Booleane note (varie notazioni usate in dump/legacy)
        fixBoolean(fixed, "require-authorization-consent");
        fixBoolean(fixed, "requireAuthorizationConsent");
        fixBoolean(fixed, "require-proof-key");
        fixBoolean(fixed, "requireProofKey");
        return ClientSettings.withSettings(fixed).build();
    }

    private TokenSettings buildTokenSettings(Map<String, Object> raw) {
        Map<String, Object> fixed = new HashMap<>(raw);

        // Durations (accetta kebab-case e snake_case)
        fixDuration(fixed, "authorization_code.time-to-live");
        fixDuration(fixed, "authorization_code_time_to_live");

        fixDuration(fixed, "access_token.time-to-live");
        fixDuration(fixed, "access_token_time_to_live");

        fixDuration(fixed, "refresh_token.time-to-live");
        fixDuration(fixed, "refresh_token_time_to_live");

        fixDuration(fixed, "device_code.time-to-live");
        fixDuration(fixed, "device_code_time_to_live");

        // Booleane
        fixBoolean(fixed, "reuse_refresh_tokens");
        fixBoolean(fixed, "reuse-refresh-tokens");

        return TokenSettings.withSettings(fixed).build();
    }

    private void fixDuration(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null || v instanceof Duration) return;
        if (v instanceof Number num) {
            m.put(key, Duration.ofMillis(num.longValue()));
            return;
        }
        if (v instanceof CharSequence cs) {
            String s = cs.toString().trim();
            // ISO-8601 (PT5M, P30D, …)
            try { m.put(key, Duration.parse(s)); return; } catch (Exception ignored) {}
            // fallback: secondi interi
            try { m.put(key, Duration.ofSeconds(Long.parseLong(s))); } catch (Exception ignored) {}
        }
    }

    private void fixBoolean(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null || v instanceof Boolean) return;
        if (v instanceof Number num) {
            m.put(key, num.intValue() != 0);
            return;
        }
        if (v instanceof CharSequence cs) {
            m.put(key, Boolean.parseBoolean(cs.toString().trim()));
        }
    }

    // ----------------------------
    // Risoluzione costanti
    // ----------------------------
    private static AuthorizationGrantType resolveAuthorizationGrantType(String v) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(v)) return AuthorizationGrantType.AUTHORIZATION_CODE;
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(v)) return AuthorizationGrantType.CLIENT_CREDENTIALS;
        if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(v)) return AuthorizationGrantType.REFRESH_TOKEN;
        if ("urn:ietf:params:oauth:grant-type:device_code".equals(v)) return AuthorizationGrantType.DEVICE_CODE;
        return new AuthorizationGrantType(v);
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String v) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(v)) return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(v)) return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        if (ClientAuthenticationMethod.NONE.getValue().equals(v)) return ClientAuthenticationMethod.NONE;
        return new ClientAuthenticationMethod(v);
    }
}
