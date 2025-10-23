package com.dietiestates.authserver.finder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dietiestates.authserver.model.DefaultAccount;

public interface DefaultAccountFinder {

	DefaultAccount getDefaultAccountByEmail(String email)
		throws UsernameNotFoundException;
}
