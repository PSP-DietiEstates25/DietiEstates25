package com.dietiestates.authorization.serviceImpl;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.dietiestates.authorization.model.Authorization;
import com.dietiestates.authorization.repository.AuthorizationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {
    private static final Logger log = LoggerFactory.getLogger(JpaOAuth2AuthorizationService.class);

    private final AuthorizationRepository authorizationRepository;
    private final RegisteredClientRepository registeredClientRepository;
    private final ObjectMapper objectMapper;

    public JpaOAuth2AuthorizationService(
            AuthorizationRepository authorizationRepository,
            RegisteredClientRepository registeredClientRepository,
            ObjectMapper objectMapper) {
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
            result = this.authorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(token);
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
        RegisteredClient rc = this.registeredClientRepository.findById(entity.getRegisteredClientId());
        if (rc == null) {
            throw new DataRetrievalFailureException("RegisteredClient id '" + entity.getRegisteredClientId() + "' not found.");
        }

        OAuth2Authorization.Builder b = OAuth2Authorization.withRegisteredClient(rc)
            .id(entity.getId())
            .principalName(entity.getPrincipalName())
            .authorizationGrantType(resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
            .authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
            .attributes(attrs -> attrs.putAll(parseMap(entity.getAttributes())));

        if (entity.getState() != null) b.attribute(OAuth2ParameterNames.STATE, entity.getState());

        var codeTok = entity.getAuthorizationCodeValue();
        if (codeTok != null) {
            OAuth2AuthorizationCode code = new OAuth2AuthorizationCode(codeTok, entity.getAuthorizationCodeIssuedAt(), entity.getAuthorizationCodeExpiresAt());
            b.token(code, md -> md.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
        }

        var atVal = entity.getAccessTokenValue();
        if (atVal != null) {
            OAuth2AccessToken at = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, atVal,
                    entity.getAccessTokenIssuedAt(), entity.getAccessTokenExpiresAt(),
                    StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
            b.token(at, md -> md.putAll(parseMap(entity.getAccessTokenMetadata())));
        }

        var rtVal = entity.getRefreshTokenValue();
        if (rtVal != null) {
            OAuth2RefreshToken rt = new OAuth2RefreshToken(rtVal, entity.getRefreshTokenIssuedAt(), entity.getRefreshTokenExpiresAt());
            b.token(rt, md -> md.putAll(parseMap(entity.getRefreshTokenMetadata())));
        }

        var idVal = entity.getOidcIdTokenValue();
        if (idVal != null) {
            OidcIdToken idt = new OidcIdToken(idVal, entity.getOidcIdTokenIssuedAt(), entity.getOidcIdTokenExpiresAt(), parseMap(entity.getOidcIdTokenClaims()));
            b.token(idt, md -> md.putAll(parseMap(entity.getOidcIdTokenMetadata())));
        }

        var ucVal = entity.getUserCodeValue();
        if (ucVal != null) {
            OAuth2UserCode uc = new OAuth2UserCode(ucVal, entity.getUserCodeIssuedAt(), entity.getUserCodeExpiresAt());
            b.token(uc, md -> md.putAll(parseMap(entity.getUserCodeMetadata())));
        }

        var dcVal = entity.getDeviceCodeValue();
        if (dcVal != null) {
            OAuth2DeviceCode dc = new OAuth2DeviceCode(dcVal, entity.getDeviceCodeIssuedAt(), entity.getDeviceCodeExpiresAt());
            b.token(dc, md -> md.putAll(parseMap(entity.getDeviceCodeMetadata())));
        }

        return b.build();
    }

    private Authorization toEntity(OAuth2Authorization a) {
        Authorization e = new Authorization();
        e.setId(a.getId());
        e.setRegisteredClientId(a.getRegisteredClientId());
        e.setPrincipalName(a.getPrincipalName());
        e.setAuthorizationGrantType(a.getAuthorizationGrantType().getValue());
        e.setAuthorizedScopes(StringUtils.collectionToDelimitedString(a.getAuthorizedScopes(), ","));
        e.setAttributes(writeMap(a.getAttributes()));
        e.setState(a.getAttribute(OAuth2ParameterNames.STATE));

        setTokenValues(a.getToken(OAuth2AuthorizationCode.class),
            e::setAuthorizationCodeValue, e::setAuthorizationCodeIssuedAt, e::setAuthorizationCodeExpiresAt, e::setAuthorizationCodeMetadata);

        OAuth2Authorization.Token<OAuth2AccessToken> at = a.getToken(OAuth2AccessToken.class);
        setTokenValues(at,
            e::setAccessTokenValue, e::setAccessTokenIssuedAt, e::setAccessTokenExpiresAt, e::setAccessTokenMetadata);
        if (at != null && at.getToken().getScopes() != null) {
            e.setAccessTokenScopes(StringUtils.collectionToDelimitedString(at.getToken().getScopes(), ","));
        }

        setTokenValues(a.getToken(OAuth2RefreshToken.class),
            e::setRefreshTokenValue, e::setRefreshTokenIssuedAt, e::setRefreshTokenExpiresAt, e::setRefreshTokenMetadata);

        OAuth2Authorization.Token<OidcIdToken> idt = a.getToken(OidcIdToken.class);
        setTokenValues(idt,
            e::setOidcIdTokenValue, e::setOidcIdTokenIssuedAt, e::setOidcIdTokenExpiresAt, e::setOidcIdTokenMetadata);
        if (idt != null) e.setOidcIdTokenClaims(writeMap(idt.getClaims()));

        setTokenValues(a.getToken(OAuth2UserCode.class),
            e::setUserCodeValue, e::setUserCodeIssuedAt, e::setUserCodeExpiresAt, e::setUserCodeMetadata);

        setTokenValues(a.getToken(OAuth2DeviceCode.class),
            e::setDeviceCodeValue, e::setDeviceCodeIssuedAt, e::setDeviceCodeExpiresAt, e::setDeviceCodeMetadata);

        return e;
    }

    private void setTokenValues(
            OAuth2Authorization.Token<?> token,
            Consumer<String> valueC,
            Consumer<Instant> issuedC,
            Consumer<Instant> expC,
            Consumer<String> metaC) {
        if (token != null) {
            OAuth2Token t = token.getToken();
            valueC.accept(t.getTokenValue());
            issuedC.accept(t.getIssuedAt());
            expC.accept(t.getExpiresAt());
            if (token.getMetadata() != null && !token.getMetadata().isEmpty()) {
                metaC.accept(writeMap(token.getMetadata()));
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
