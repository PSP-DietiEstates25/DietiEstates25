package com.dietiestates.authserver.serviceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietiestates.authserver.finder.DefaultAccountFinder;
import com.dietiestates.authserver.model.SecurityAccountDecorator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl {//implements UserDetailsService {

	/*
	private final DefaultAccountFinder defaultAccountFinder;
	
	//@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		var account = defaultAccountFinder.getDefaultAccountByEmail(email);
		
		return SecurityAccountDecorator.builder()
				.defaultAccount(account)
				.enabled(true)
				.locked(false)
				.build();
	}
	*/
}
