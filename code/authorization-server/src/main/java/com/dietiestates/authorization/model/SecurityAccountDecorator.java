package com.dietiestates.authorization.model;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityAccountDecorator implements UserDetails, Principal, Serializable {

    private static final long serialVersionUID = 6511396009113031709L;

    private DefaultAccount defaultAccount;
    private Boolean enabled;
    private Boolean locked;

    //temporaneo per la deserializzazione delle authorities
    private Collection<? extends GrantedAuthority> tempAuthorities;

    @Builder(builderMethodName = "builder")
    public SecurityAccountDecorator(DefaultAccount defaultAccount, Boolean enabled, Boolean locked) {
        this.defaultAccount = defaultAccount;
        this.enabled = enabled;
        this.locked = locked;
    }

    @JsonIgnore
    public Long getAccountId() {
        return defaultAccount != null ? defaultAccount.getAccountId() : null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return defaultAccount != null ? defaultAccount.getAccountEmail() : null;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return defaultAccount != null ? defaultAccount.getAccountPassword() : null;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (tempAuthorities != null && !tempAuthorities.isEmpty()) {
            return tempAuthorities;
        }

        if (defaultAccount == null || defaultAccount.getAccountRole() == null) {
            return List.of();
        }

        return List.of(defaultAccount.getAccountRole()).stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                .collect(Collectors.toList());
    }

    @JsonProperty("authorities")
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.tempAuthorities = authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(locked);
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}
