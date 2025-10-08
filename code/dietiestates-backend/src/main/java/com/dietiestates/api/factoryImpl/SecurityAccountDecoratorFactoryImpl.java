package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.SecurityAccountDecorator;

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
