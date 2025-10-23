package com.dietiestates.authorization.factory;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.dietiestates.authorization.model.DefaultAccount;
import com.dietiestates.authorization.model.Role;
import com.dietiestates.authorization.spec.AccountSpec;

public interface DefaultAccountFactory {

	DefaultAccount createAccountFromSpec(
			AccountSpec defaultAccount,
			PasswordEncoder passwordEncoder,
			Role role
			);
}
