package com.dietiestates.auth.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "oauth2_authorization")
public class Authorization {
    @Id
    @Column
    private String id;

    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;

    // small string ok con length
    @Column(length = 1000)
    private String authorizedScopes;

    // ====== GRANDI STRINGHE -> TEXT/LONGVARCHAR (NO @Lob) ======
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String attributes;

    @Column(length = 500)
    private String state;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String authorizationCodeValue;
    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String authorizationCodeMetadata;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String accessTokenValue;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String accessTokenMetadata;

    private String accessTokenType;

    @Column(length = 1000)
    private String accessTokenScopes;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String refreshTokenValue;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String refreshTokenMetadata;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String oidcIdTokenValue;
    private Instant oidcIdTokenIssuedAt;
    private Instant oidcIdTokenExpiresAt;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String oidcIdTokenMetadata;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String oidcIdTokenClaims;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String userCodeValue;
    private Instant userCodeIssuedAt;
    private Instant userCodeExpiresAt;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String userCodeMetadata;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String deviceCodeValue;
    private Instant deviceCodeIssuedAt;
    private Instant deviceCodeExpiresAt;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(columnDefinition = "text")
    private String deviceCodeMetadata;

    // ====== GETTER/SETTER ======

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRegisteredClientId() { return registeredClientId; }
    public void setRegisteredClientId(String registeredClientId) { this.registeredClientId = registeredClientId; }

    public String getPrincipalName() { return principalName; }
    public void setPrincipalName(String principalName) { this.principalName = principalName; }

    public String getAuthorizationGrantType() { return authorizationGrantType; }
    public void setAuthorizationGrantType(String authorizationGrantType) { this.authorizationGrantType = authorizationGrantType; }

    public String getAuthorizedScopes() { return this.authorizedScopes; }
    public void setAuthorizedScopes(String authorizedScopes) { this.authorizedScopes = authorizedScopes; }

    public String getAttributes() { return attributes; }
    public void setAttributes(String attributes) { this.attributes = attributes; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getAuthorizationCodeValue() { return authorizationCodeValue; }
    public void setAuthorizationCodeValue(String authorizationCode) { this.authorizationCodeValue = authorizationCode; }

    public Instant getAuthorizationCodeIssuedAt() { return authorizationCodeIssuedAt; }
    public void setAuthorizationCodeIssuedAt(Instant v) { this.authorizationCodeIssuedAt = v; }

    public Instant getAuthorizationCodeExpiresAt() { return authorizationCodeExpiresAt; }
    public void setAuthorizationCodeExpiresAt(Instant v) { this.authorizationCodeExpiresAt = v; }

    public String getAuthorizationCodeMetadata() { return authorizationCodeMetadata; }
    public void setAuthorizationCodeMetadata(String v) { this.authorizationCodeMetadata = v; }

    public String getAccessTokenValue() { return accessTokenValue; }
    public void setAccessTokenValue(String accessToken) { this.accessTokenValue = accessToken; }

    public Instant getAccessTokenIssuedAt() { return accessTokenIssuedAt; }
    public void setAccessTokenIssuedAt(Instant v) { this.accessTokenIssuedAt = v; }

    public Instant getAccessTokenExpiresAt() { return accessTokenExpiresAt; }
    public void setAccessTokenExpiresAt(Instant v) { this.accessTokenExpiresAt = v; }

    public String getAccessTokenMetadata() { return accessTokenMetadata; }
    public void setAccessTokenMetadata(String v) { this.accessTokenMetadata = v; }

    public String getAccessTokenType() { return accessTokenType; }
    public void setAccessTokenType(String accessTokenType) { this.accessTokenType = accessTokenType; }

    public String getAccessTokenScopes() { return accessTokenScopes; }
    public void setAccessTokenScopes(String accessTokenScopes) { this.accessTokenScopes = accessTokenScopes; }

    public String getRefreshTokenValue() { return refreshTokenValue; }
    public void setRefreshTokenValue(String refreshToken) { this.refreshTokenValue = refreshToken; }

    public Instant getRefreshTokenIssuedAt() { return refreshTokenIssuedAt; }
    public void setRefreshTokenIssuedAt(Instant v) { this.refreshTokenIssuedAt = v; }

    public Instant getRefreshTokenExpiresAt() { return refreshTokenExpiresAt; }
    public void setRefreshTokenExpiresAt(Instant v) { this.refreshTokenExpiresAt = v; }

    public String getRefreshTokenMetadata() { return refreshTokenMetadata; }
    public void setRefreshTokenMetadata(String v) { this.refreshTokenMetadata = v; }

    public String getOidcIdTokenValue() { return oidcIdTokenValue; }
    public void setOidcIdTokenValue(String idToken) { this.oidcIdTokenValue = idToken; }

    public Instant getOidcIdTokenIssuedAt() { return oidcIdTokenIssuedAt; }
    public void setOidcIdTokenIssuedAt(Instant v) { this.oidcIdTokenIssuedAt = v; }

    public Instant getOidcIdTokenExpiresAt() { return oidcIdTokenExpiresAt; }
    public void setOidcIdTokenExpiresAt(Instant v) { this.oidcIdTokenExpiresAt = v; }

    public String getOidcIdTokenMetadata() { return oidcIdTokenMetadata; }
    public void setOidcIdTokenMetadata(String v) { this.oidcIdTokenMetadata = v; }

    public String getOidcIdTokenClaims() { return oidcIdTokenClaims; }
    public void setOidcIdTokenClaims(String v) { this.oidcIdTokenClaims = v; }

    public String getUserCodeValue() { return this.userCodeValue; }
    public void setUserCodeValue(String v) { this.userCodeValue = v; }

    public Instant getUserCodeIssuedAt() { return this.userCodeIssuedAt; }
    public void setUserCodeIssuedAt(Instant v) { this.userCodeIssuedAt = v; }

    public Instant getUserCodeExpiresAt() { return this.userCodeExpiresAt; }
    public void setUserCodeExpiresAt(Instant v) { this.userCodeExpiresAt = v; }

    public String getUserCodeMetadata() { return this.userCodeMetadata; }
    public void setUserCodeMetadata(String v) { this.userCodeMetadata = v; }

    public String getDeviceCodeValue() { return this.deviceCodeValue; }
    public void setDeviceCodeValue(String v) { this.deviceCodeValue = v; }

    public Instant getDeviceCodeIssuedAt() { return this.deviceCodeIssuedAt; }
    public void setDeviceCodeIssuedAt(Instant v) { this.deviceCodeIssuedAt = v; }

    public Instant getDeviceCodeExpiresAt() { return this.deviceCodeExpiresAt; }
    public void setDeviceCodeExpiresAt(Instant v) { this.deviceCodeExpiresAt = v; }

    public String getDeviceCodeMetadata() { return this.deviceCodeMetadata; }
    public void setDeviceCodeMetadata(String v) { this.deviceCodeMetadata = v; }
}
