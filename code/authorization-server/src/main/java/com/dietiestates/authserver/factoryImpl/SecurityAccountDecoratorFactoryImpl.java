package com.dietiestates.authserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.authserver.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.authserver.model.DefaultAccount;
import com.dietiestates.authserver.model.SecurityAccountDecorator;

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
