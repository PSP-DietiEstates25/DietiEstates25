package com.authenticationserver.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.authenticationserver.api.factory.SecurityAccountDecoratorFactory;
import com.authenticationserver.api.model.DefaultAccount;
import com.authenticationserver.api.model.SecurityAccountDecorator;

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
