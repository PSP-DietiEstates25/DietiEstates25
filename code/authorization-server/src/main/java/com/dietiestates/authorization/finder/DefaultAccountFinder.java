package com.dietiestates.authorization.finder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dietiestates.authorization.model.DefaultAccount;

public interface DefaultAccountFinder {

	DefaultAccount getDefaultAccountByEmail(String email)
		throws UsernameNotFoundException;
}
