package com.authenticationserver.api.factory;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.authenticationserver.api.model.DefaultAccount;
import com.authenticationserver.api.model.Role;
import com.authenticationserver.api.spec.AccountSpec;

public interface DefaultAccountFactory {

	DefaultAccount createAccountFromSpec(
			AccountSpec defaultAccount,
			PasswordEncoder passwordEncoder,
			Role role
			);
}
