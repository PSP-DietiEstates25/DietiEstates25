package com.authenticationserver.api.finderImpl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.authenticationserver.api.finder.DefaultAccountFinder;
import com.authenticationserver.api.model.DefaultAccount;
import com.authenticationserver.api.repository.DefaultAccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultAccountFinderImpl implements DefaultAccountFinder {

	private final DefaultAccountRepository defaultAccountRepository;

	@Override
	public DefaultAccount getDefaultAccountByEmail(String email) {
		return defaultAccountRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Account with specified email not found"));
	}
	
}
