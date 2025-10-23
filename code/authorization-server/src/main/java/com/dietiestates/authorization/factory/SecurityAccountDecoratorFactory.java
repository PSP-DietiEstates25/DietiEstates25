package com.dietiestates.authorization.factory;

import com.dietiestates.authorization.model.DefaultAccount;
import com.dietiestates.authorization.model.SecurityAccountDecorator;

public interface SecurityAccountDecoratorFactory {

	SecurityAccountDecorator createSecurityAccountDecoratorFromSpec(
			DefaultAccount defaultAccount
			);
}
