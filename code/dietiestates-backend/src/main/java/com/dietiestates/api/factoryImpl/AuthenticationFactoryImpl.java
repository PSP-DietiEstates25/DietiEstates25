package com.dietiestates.api.factoryImpl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.AuthenticationFactory;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.AuthenticationSpec;

import lombok.RequiredArgsConstructor;

@Component("authenticationFactoryImpl")
@Primary
@RequiredArgsConstructor
public class AuthenticationFactoryImpl implements AuthenticationFactory {

	@Override
	public User createAccountFromSpec(
			AuthenticationSpec spec,
			PasswordEncoder passwordEncoder,
			Role role
		) {
		return User.userBuilder()
				.email(spec.getEmail())
				.password(passwordEncoder.encode(spec.getPassword()))
				.accountLocked(false)
				.enabled(true)
				.roles(List.of(role))
				.build();
	}
	
}
