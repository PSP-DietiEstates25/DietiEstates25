package com.dietiestates.authserver.factoryImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.authserver.factory.DefaultAccountFactory;
import com.dietiestates.authserver.model.DefaultAccount;
import com.dietiestates.authserver.model.Role;
import com.dietiestates.authserver.spec.AccountSpec;

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
