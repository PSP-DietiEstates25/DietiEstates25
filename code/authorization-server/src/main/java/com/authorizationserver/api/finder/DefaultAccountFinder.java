package com.authorizationserver.api.finder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.authorizationserver.api.model.DefaultAccount;

public interface DefaultAccountFinder {

	DefaultAccount getDefaultAccountByEmail(String email)
		throws UsernameNotFoundException;
}
