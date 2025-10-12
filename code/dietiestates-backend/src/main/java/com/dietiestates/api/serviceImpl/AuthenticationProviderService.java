package com.dietiestates.api.serviceImpl;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationProviderService 
	implements AuthenticationProvider {

	private final UserDetailsServiceImpl userDetailsService;
	//private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		var user = userDetailsService.loadUserByUsername(email);
		
		//return this.checkPassword(user, password);
		return new UsernamePasswordAuthenticationToken(
				user.getUsername(),
				user.getPassword(),
				user.getAuthorities()
				);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	/*
	private Authentication checkPassword(UserDetails user, String rawPassword) {
		
		if(bCryptPasswordEncoder.matches(rawPassword, user.getPassword())) {
			return new UsernamePasswordAuthenticationToken(
					user.getUsername(),
					user.getPassword(),
					user.getAuthorities()
					);
		} else {
			throw new BadCredentialsException("Bad credentials");
		}
	}
	*/
}
