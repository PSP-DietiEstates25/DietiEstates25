package com.dietiestates.auth.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.dietiestates.auth.model.AuthorizationConsent;
import com.dietiestates.auth.repository.AuthorizationConsentRepository;

@Component
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    private final AuthorizationConsentRepository repository;
    private final RegisteredClientRepository registeredClientRepository;

    public JpaOAuth2AuthorizationConsentService(AuthorizationConsentRepository repository,
                                                RegisteredClientRepository registeredClientRepository) {
        Assert.notNull(repository, "authorizationConsentRepository cannot be null");
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        this.repository = repository;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void save(OAuth2AuthorizationConsent consent) {
        Assert.notNull(consent, "authorizationConsent cannot be null");
        this.repository.save(toEntity(consent));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent consent) {
        Assert.notNull(consent, "authorizationConsent cannot be null");
        this.repository.deleteByRegisteredClientIdAndPrincipalName(consent.getRegisteredClientId(), consent.getPrincipalName());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return this.repository.findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName)
                .map(this::toObject)
                .orElse(null);
    }

    private OAuth2AuthorizationConsent toObject(AuthorizationConsent entity) {
        String registeredClientId = entity.getRegisteredClientId();
        RegisteredClient rc = this.registeredClientRepository.findById(registeredClientId);
        if (rc == null) {
            throw new DataRetrievalFailureException("RegisteredClient id '" + registeredClientId + "' not found.");
        }
        OAuth2AuthorizationConsent.Builder b = OAuth2AuthorizationConsent.withId(registeredClientId, entity.getPrincipalName());
        if (entity.getAuthorities() != null) {
            for (String a : StringUtils.commaDelimitedListToSet(entity.getAuthorities())) {
                b.authority(new SimpleGrantedAuthority(a));
            }
        }
        return b.build();
    }

    private AuthorizationConsent toEntity(OAuth2AuthorizationConsent c) {
        AuthorizationConsent e = new AuthorizationConsent();
        e.setRegisteredClientId(c.getRegisteredClientId());
        e.setPrincipalName(c.getPrincipalName());
        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority a : c.getAuthorities()) {
            authorities.add(a.getAuthority());
        }
        e.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));
        return e;
    }
}

