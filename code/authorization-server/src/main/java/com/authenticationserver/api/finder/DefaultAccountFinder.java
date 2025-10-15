package com.authenticationserver.api.finder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.authenticationserver.api.model.DefaultAccount;

public interface DefaultAccountFinder {

	DefaultAccount getDefaultAccountByEmail(String email)
		throws UsernameNotFoundException;
}
