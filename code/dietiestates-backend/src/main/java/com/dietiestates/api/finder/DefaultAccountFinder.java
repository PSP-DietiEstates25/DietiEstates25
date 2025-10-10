package com.dietiestates.api.finder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dietiestates.api.model.DefaultAccount;

public interface DefaultAccountFinder {

	DefaultAccount getDefaultAccountByEmail(String email)
		throws UsernameNotFoundException;
}
