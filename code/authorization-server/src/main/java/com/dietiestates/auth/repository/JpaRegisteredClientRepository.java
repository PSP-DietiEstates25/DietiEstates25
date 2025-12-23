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
                .clientAuthenticationMethods(clientAuthenticationMethodsConsumer ->
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                clientAuthenticationMethodsConsumer.add(resolveClientAuthenticationMethod(authenticationMethod))
                        )
                )
                .authorizationGrantTypes(grantTypeConsumer ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypeConsumer.add(resolveAuthorizationGrantType(grantType))
                        )
                )
                .redirectUris(uris -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                .scopes(scopes -> scopes.addAll(clientScopes));

        Map<String, Object> clientSettingsMap = parseMap(client.getClientSettings());
        builder.clientSettings(buildClientSettings(clientSettingsMap));

        Map<String, Object> tokenSettingsMap = parseMap(client.getTokenSettings());
        builder.tokenSettings(buildTokenSettings(tokenSettingsMap));

        return builder.build();
    }

    private Client toEntity(RegisteredClient registeredClient) {
        List<String> authMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
        registeredClient.getClientAuthenticationMethods().forEach(cam -> authMethods.add(cam.getValue()));

        List<String> grantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
        registeredClient.getAuthorizationGrantTypes().forEach(gt -> grantTypes.add(gt.getValue()));

        Client client = new Client();
        client.setId(registeredClient.getId());
        client.setClientId(registeredClient.getClientId());
        client.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        client.setClientSecret(registeredClient.getClientSecret());
        client.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        client.setClientName(registeredClient.getClientName());
        client.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(authMethods));
        client.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(grantTypes));
        client.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        client.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
        client.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        client.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        client.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));
        return client;
    }

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

    private ClientSettings buildClientSettings(Map<String, Object> raw) {
        Map<String, Object> fixed = new HashMap<>(raw);
        fixBoolean(fixed, "require-authorization-consent");
        fixBoolean(fixed, "requireAuthorizationConsent");
        fixBoolean(fixed, "require-proof-key");
        fixBoolean(fixed, "requireProofKey");
        return ClientSettings.withSettings(fixed).build();
    }

    private TokenSettings buildTokenSettings(Map<String, Object> raw) {
        Map<String, Object> fixed = new HashMap<>(raw);
        fixDuration(fixed, "authorization_code.time-to-live");
        fixDuration(fixed, "authorization_code_time_to_live");

        fixDuration(fixed, "access_token.time-to-live");
        fixDuration(fixed, "access_token_time_to_live");

        fixDuration(fixed, "refresh_token.time-to-live");
        fixDuration(fixed, "refresh_token_time_to_live");

        fixDuration(fixed, "device_code.time-to-live");
        fixDuration(fixed, "device_code_time_to_live");

        fixBoolean(fixed, "reuse_refresh_tokens");
        fixBoolean(fixed, "reuse-refresh-tokens");

        return TokenSettings.withSettings(fixed).build();
    }

    private void fixDuration(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || value instanceof Duration) return;
        if (value instanceof Number num) {
            map.put(key, Duration.ofMillis(num.longValue()));
            return;
        }
        if (value instanceof CharSequence charSequence) {
            String string = charSequence.toString().trim();
            try { map.put(key, Duration.parse(string)); return; } catch (Exception ignored) {}
            try { map.put(key, Duration.ofSeconds(Long.parseLong(string))); } catch (Exception ignored) {}
        }
    }

    private void fixBoolean(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || value instanceof Boolean) return;
        if (value instanceof Number num) {
            map.put(key, num.intValue() != 0);
            return;
        }
        if (value instanceof CharSequence charSequence) {
            map.put(key, Boolean.parseBoolean(charSequence.toString().trim()));
        }
    }

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
