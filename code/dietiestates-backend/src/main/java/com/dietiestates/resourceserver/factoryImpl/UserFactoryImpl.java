package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.UserFactory;
import com.dietiestates.resourceserver.model.User;

import lombok.RequiredArgsConstructor;

@Component
@Primary
@RequiredArgsConstructor
public class UserFactoryImpl implements UserFactory {

	@Override
	public User createUserFromSpec(
			String email
			) {
		return User.builder()
				.email(email)
				.build();
	}
	
}
