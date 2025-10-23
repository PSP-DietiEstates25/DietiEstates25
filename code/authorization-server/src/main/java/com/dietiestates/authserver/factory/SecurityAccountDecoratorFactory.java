package com.dietiestates.authserver.factory;

import com.dietiestates.authserver.model.DefaultAccount;
import com.dietiestates.authserver.model.SecurityAccountDecorator;

public interface SecurityAccountDecoratorFactory {

	SecurityAccountDecorator createSecurityAccountDecoratorFromSpec(
			DefaultAccount defaultAccount
			);
}
