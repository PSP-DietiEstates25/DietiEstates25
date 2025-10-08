package com.dietiestates.api.factoryImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.UserFactory;
import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.User;

import lombok.RequiredArgsConstructor;

@Component
@Primary
@RequiredArgsConstructor
public class UserFactoryImpl implements UserFactory {

	@Override
	public User createUserFromSpec(
			DefaultAccount securityAccountDecorator
			) {
		return User.builder()
				.securityAccountDecorator(securityAccountDecorator)
				.build();
	}
	
}
