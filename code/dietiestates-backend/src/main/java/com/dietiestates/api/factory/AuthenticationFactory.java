package com.dietiestates.api.factory;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.dietiestates.api.model.Role;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.AuthenticationSpec;

public interface AuthenticationFactory {

	User createAccountFromSpec(
			AuthenticationSpec spec,
			PasswordEncoder encoder,
			Role role
			);
	
}
