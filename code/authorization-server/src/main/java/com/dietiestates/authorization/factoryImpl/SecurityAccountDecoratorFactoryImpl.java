package com.dietiestates.authorization.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.authorization.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.authorization.model.DefaultAccount;
import com.dietiestates.authorization.model.SecurityAccountDecorator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityAccountDecoratorFactoryImpl implements SecurityAccountDecoratorFactory {

	@Override
	public SecurityAccountDecorator createSecurityAccountDecoratorFromSpec(
			DefaultAccount defaultAccount
			) {
		return SecurityAccountDecorator.builder()
				.defaultAccount(defaultAccount)
				.enabled(true)
				.locked(false)
				.build();
	}
}
