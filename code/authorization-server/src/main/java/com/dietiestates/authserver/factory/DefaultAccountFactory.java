package com.dietiestates.authserver.factory;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.dietiestates.authserver.model.DefaultAccount;
import com.dietiestates.authserver.model.Role;
import com.dietiestates.authserver.spec.AccountSpec;

public interface DefaultAccountFactory {

	DefaultAccount createAccountFromSpec(
			AccountSpec defaultAccount,
			PasswordEncoder passwordEncoder,
			Role role
			);
}
