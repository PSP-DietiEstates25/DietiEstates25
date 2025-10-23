package com.dietiestates.authorization.finderImpl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.dietiestates.authorization.finder.DefaultAccountFinder;
import com.dietiestates.authorization.model.DefaultAccount;
import com.dietiestates.authorization.repository.DefaultAccountRepository;

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
