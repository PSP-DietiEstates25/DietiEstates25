package com.dietiestates.api.factory;

import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.SecurityAccountDecorator;

public interface SecurityAccountDecoratorFactory {

	SecurityAccountDecorator createSecurityAccountDecoratorFromSpec(
			DefaultAccount defaultAccount
			);
}
