package com.authorizationserver.api.factory;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.authorizationserver.api.model.DefaultAccount;
import com.authorizationserver.api.model.Role;
import com.authorizationserver.api.spec.AccountSpec;

public interface DefaultAccountFactory {

	DefaultAccount createAccountFromSpec(
			AccountSpec defaultAccount,
			PasswordEncoder passwordEncoder,
			Role role
			);
}
