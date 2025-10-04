package com.dietiestates.api.factory;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.spec.StafferSpec;

public interface EstateAgentFactory extends AuthenticationFactory {

	EstateAgent createEstateAgentFromSpec(
			StafferSpec spec,
			PasswordEncoder passwordEncoder,
			Role estateAgentRole,
			Admin admin
			);
	
}
