package com.dietiestates.authserver.model;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SecurityAccountDecorator extends AccountDecorator implements UserDetails, Principal {

	private static final long serialVersionUID = -7873878904268523666L;
	private Boolean enabled;
	private Boolean locked;
	
	@Builder(builderMethodName = "builder")
	public SecurityAccountDecorator(
			DefaultAccount defaultAccount,
			Boolean enabled,
			Boolean locked
			) {
		super(defaultAccount);
		this.enabled = enabled;
		this.locked = locked;
	}
	
	@Override
	public String getUsername() {
		return getAccountEmail();
	}

	@Override
	public String getName() {
		return getAccountEmail();
	}

	@Override
	public String getPassword() {
		return getAccountPassword();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return List.of(getAccountRole()).stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().toString()))
				.collect(Collectors.toList());
	}
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
}