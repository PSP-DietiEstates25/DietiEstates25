package com.dietiestates.auth.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.dietiestates.auth.model.Authorization;
import com.dietiestates.auth.repository.AuthorizationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(JpaOAuth2AuthorizationService.class);

    private final AuthorizationRepository authorizationRepository;
    private final RegisteredClientRepository registeredClientRepository;
    private final ObjectMapper objectMapper;

    public JpaOAuth2AuthorizationService(
            AuthorizationRepository authorizationRepository,
            RegisteredClientRepository registeredClientRepository,
            @Qualifier("authorizationObjectMapper") ObjectMapper objectMapper) {
        Assert.notNull(authorizationRepository, "authorizationRepository cannot be null");
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        Assert.notNull(objectMapper, "objectMapper cannot be null");
        this.authorizationRepository = authorizationRepository;
        this.registeredClientRepository = registeredClientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.authorizationRepository.save(toEntity(authorization));
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.authorizationRepository.deleteById(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.authorizationRepository.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Optional<Authorization> result;
        if (tokenType == null) {
            result = this.authorizationRepository
                    .findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            result = this.authorizationRepository.findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            result = this.authorizationRepository.findByAuthorizationCodeValue(token);
        } else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
            result = this.authorizationRepository.findByAccessTokenValue(token);
        } else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
            result = this.authorizationRepository.findByRefreshTokenValue(token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            result = this.authorizationRepository.findByOidcIdTokenValue(token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            result = this.authorizationRepository.findByUserCodeValue(token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            result = this.authorizationRepository.findByDeviceCodeValue(token);
        } else {
            result = Optional.empty();
        }
        return result.map(this::toObject).orElse(null);
    }

    private OAuth2Authorization toObject(Authorization entity) {
        RegisteredClient registeredClient = this.registeredClientRepository.findById(entity.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "RegisteredClient id '" + entity.getRegisteredClientId() + "' not found.");
        }

        OAuth2Authorization.Builder oAuth2AuthorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
                .authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
                .attributes(attributes -> attributes.putAll(parseMap(entity.getAttributes())));

        if (entity.getState() != null) {
            oAuth2AuthorizationBuilder.attribute(OAuth2ParameterNames.STATE, entity.getState());
        }

        var authorizationCodeValue = entity.getAuthorizationCodeValue();
        if (authorizationCodeValue != null) {
            OAuth2AuthorizationCode code = new OAuth2AuthorizationCode(
                    authorizationCodeValue, entity.getAuthorizationCodeIssuedAt(), entity.getAuthorizationCodeExpiresAt());
            oAuth2AuthorizationBuilder.token(code, metaData -> metaData.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
        }

        var accessTokenValue = entity.getAccessTokenValue();
        if (accessTokenValue != null) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER, accessTokenValue,
                    entity.getAccessTokenIssuedAt(), entity.getAccessTokenExpiresAt(),
                    StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
            oAuth2AuthorizationBuilder.token(accessToken, metaData -> metaData.putAll(parseMap(entity.getAccessTokenMetadata())));
        }

        var refreshTokenValue = entity.getRefreshTokenValue();
        if (refreshTokenValue != null) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    refreshTokenValue, entity.getRefreshTokenIssuedAt(), entity.getRefreshTokenExpiresAt());
            oAuth2AuthorizationBuilder.token(refreshToken, metaData -> metaData.putAll(parseMap(entity.getRefreshTokenMetadata())));
        }

        var oidcIdTokenValue = entity.getOidcIdTokenValue();
        if (oidcIdTokenValue != null) {
            OidcIdToken token = new OidcIdToken(
                    oidcIdTokenValue, entity.getOidcIdTokenIssuedAt(), entity.getOidcIdTokenExpiresAt(),
                    parseMap(entity.getOidcIdTokenClaims()));
            oAuth2AuthorizationBuilder.token(token, metaData -> metaData.putAll(parseMap(entity.getOidcIdTokenMetadata())));
        }

        var userCodeValue = entity.getUserCodeValue();
        if (userCodeValue != null) {
            OAuth2UserCode userCode = new OAuth2UserCode(
                    userCodeValue, entity.getUserCodeIssuedAt(), entity.getUserCodeExpiresAt());
            oAuth2AuthorizationBuilder.token(userCode, metaData -> metaData.putAll(parseMap(entity.getUserCodeMetadata())));
        }

        var deviceCodeValue = entity.getDeviceCodeValue();
        if (deviceCodeValue != null) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
                    deviceCodeValue, entity.getDeviceCodeIssuedAt(), entity.getDeviceCodeExpiresAt());
            oAuth2AuthorizationBuilder.token(deviceCode, metaData -> metaData.putAll(parseMap(entity.getDeviceCodeMetadata())));
        }

        return oAuth2AuthorizationBuilder.build();
    }

    private Authorization toEntity(OAuth2Authorization oAuth2Authorization) {
        Authorization authorization = new Authorization();
        authorization.setId(oAuth2Authorization.getId());
        authorization.setRegisteredClientId(oAuth2Authorization.getRegisteredClientId());
        authorization.setPrincipalName(oAuth2Authorization.getPrincipalName());
        authorization.setAuthorizationGrantType(oAuth2Authorization.getAuthorizationGrantType().getValue());
        authorization.setAuthorizedScopes(StringUtils.collectionToDelimitedString(oAuth2Authorization.getAuthorizedScopes(), ","));
        authorization.setAttributes(writeMap(oAuth2Authorization.getAttributes()));
        authorization.setState(oAuth2Authorization.getAttribute(OAuth2ParameterNames.STATE));

        setTokenValues(
                oAuth2Authorization.getToken(OAuth2AuthorizationCode.class),
                authorization::setAuthorizationCodeValue,
                authorization::setAuthorizationCodeIssuedAt,
                authorization::setAuthorizationCodeExpiresAt,
                authorization::setAuthorizationCodeMetadata
        );

        OAuth2Authorization.Token<OAuth2AccessToken> oauth2AccessToken = oAuth2Authorization.getToken(OAuth2AccessToken.class);
        setTokenValues(
                oauth2AccessToken,
                authorization::setAccessTokenValue,
                authorization::setAccessTokenIssuedAt,
                authorization::setAccessTokenExpiresAt,
                authorization::setAccessTokenMetadata
        );

        if (oauth2AccessToken != null && oauth2AccessToken.getToken().getScopes() != null) {
            authorization.setAccessTokenScopes(StringUtils.collectionToDelimitedString(oauth2AccessToken.getToken().getScopes(), ","));
        }

        //aggiunta
        setTokenValues(
                oAuth2Authorization.getToken(OAuth2RefreshToken.class),
                authorization::setRefreshTokenValue,
                authorization::setRefreshTokenIssuedAt,
                authorization::setRefreshTokenExpiresAt,
                authorization::setRefreshTokenMetadata
        );

        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = oAuth2Authorization.getToken(OidcIdToken.class);
        setTokenValues(
                oidcIdToken,
                authorization::setOidcIdTokenValue,
                authorization::setOidcIdTokenIssuedAt,
                authorization::setOidcIdTokenExpiresAt,
                authorization::setOidcIdTokenMetadata
        );
        if (oidcIdToken != null) {
            authorization.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
        }

        setTokenValues(
                oAuth2Authorization.getToken(OAuth2UserCode.class),
                authorization::setUserCodeValue,
                authorization::setUserCodeIssuedAt,
                authorization::setUserCodeExpiresAt,
                authorization::setUserCodeMetadata
        );

        setTokenValues(
                oAuth2Authorization.getToken(OAuth2DeviceCode.class),
                authorization::setDeviceCodeValue,
                authorization::setDeviceCodeIssuedAt,
                authorization::setDeviceCodeExpiresAt,
                authorization::setDeviceCodeMetadata
        );

        return authorization;
    }

    private void setTokenValues(
            OAuth2Authorization.Token<?> token,
            Consumer<String> valueConsumer,
            Consumer<Instant> issuedConsumer,
            Consumer<Instant> expConsumer,
            Consumer<String> metaConsumer) {
        if (token != null) {
            OAuth2Token oAuth2Token = token.getToken();
            valueConsumer.accept(oAuth2Token.getTokenValue());
            issuedConsumer.accept(oAuth2Token.getIssuedAt());
            expConsumer.accept(oAuth2Token.getExpiresAt());
            if (token.getMetadata() != null && !token.getMetadata().isEmpty()) {
                metaConsumer.accept(writeMap(token.getMetadata()));
            }
        }
    }

    private Map<String, Object> parseMap(String data) {
        try {
            if (data == null || data.isBlank() || "null".equals(data)) return Map.of();
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            log.warn("Authorization JSON parse error. Returning empty map. payload={}", data, ex);
            return Map.of();
        }
    }

    private String writeMap(Map<String, Object> data) {
        try {
            if (data == null || data.isEmpty()) return "{}";
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.warn("Authorization JSON write error. Returning empty json. map={}", data, ex);
            return "{}";
        }
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String v) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(v)) return AuthorizationGrantType.AUTHORIZATION_CODE;
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(v)) return AuthorizationGrantType.CLIENT_CREDENTIALS;
        if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(v)) return AuthorizationGrantType.REFRESH_TOKEN;
        if ("urn:ietf:params:oauth:grant-type:device_code".equals(v)) return AuthorizationGrantType.DEVICE_CODE;
        return new AuthorizationGrantType(v);
    }
}
