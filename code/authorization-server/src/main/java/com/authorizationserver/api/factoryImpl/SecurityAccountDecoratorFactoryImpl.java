package com.authorizationserver.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.authorizationserver.api.factory.SecurityAccountDecoratorFactory;
import com.authorizationserver.api.model.DefaultAccount;
import com.authorizationserver.api.model.SecurityAccountDecorator;

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
