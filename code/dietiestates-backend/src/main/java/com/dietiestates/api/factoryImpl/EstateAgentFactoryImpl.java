package com.dietiestates.api.factoryImpl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.EstateAgentFactory;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.spec.StafferSpec;

import lombok.RequiredArgsConstructor;

@Component("estateAgentFactoryImpl")
@RequiredArgsConstructor
public class EstateAgentFactoryImpl extends AuthenticationFactoryImpl implements EstateAgentFactory {

	@Override
	public EstateAgent createEstateAgentFromSpec(
			StafferSpec spec,
			PasswordEncoder passwordEncoder,
			Role adminRole,
			Admin admin
			) {
		return EstateAgent.estateAgentBuilder()
				.email(spec.getEmail())
				.password(passwordEncoder.encode(spec.getPassword()))
				.accountLocked(false)
				.enabled(false)
				.roles(List.of(adminRole))
				.admin(admin)
				.build();
	}
	
}
