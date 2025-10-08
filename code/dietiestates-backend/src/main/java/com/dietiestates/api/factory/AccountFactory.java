package com.dietiestates.api.factory;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.spec.AuthenticationSpec;

public interface AccountFactory {

	DefaultAccount createAccountFromSpec(
			AuthenticationSpec spec,
			PasswordEncoder passwordEncoder,
			Role role
			);
}
