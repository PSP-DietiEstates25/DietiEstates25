package com.dietiestates.api.factoryImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.AccountFactory;
import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.spec.AuthenticationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountFactoryImpl implements AccountFactory {

	@Override
	public DefaultAccount createAccountFromSpec(
			AuthenticationSpec spec,
			PasswordEncoder passwordEncored,
			Role role
			) {
		return DefaultAccount.builder()
				.email(spec.getEmail())
				.password(spec.getPassword())
				.role(role)
				.build();
	}

}
