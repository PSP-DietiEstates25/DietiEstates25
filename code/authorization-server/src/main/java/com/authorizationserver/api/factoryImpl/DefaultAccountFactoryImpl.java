package com.authorizationserver.api.factoryImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.authorizationserver.api.factory.DefaultAccountFactory;
import com.authorizationserver.api.model.DefaultAccount;
import com.authorizationserver.api.model.Role;
import com.authorizationserver.api.spec.AccountSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultAccountFactoryImpl implements DefaultAccountFactory {

	@Override
	public DefaultAccount createAccountFromSpec(
			AccountSpec spec,
			PasswordEncoder passwordEncoder,
			Role role
			) {
		return DefaultAccount.builder()
				.email(spec.getEmail())
				.password(passwordEncoder.encode(spec.getPassword()))
				.role(role)
				.build();
	}
}
