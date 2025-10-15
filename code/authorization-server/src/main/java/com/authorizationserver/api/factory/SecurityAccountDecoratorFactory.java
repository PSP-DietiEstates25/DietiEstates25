package com.authorizationserver.api.factory;

import com.authorizationserver.api.model.DefaultAccount;
import com.authorizationserver.api.model.SecurityAccountDecorator;

public interface SecurityAccountDecoratorFactory {

	SecurityAccountDecorator createSecurityAccountDecoratorFromSpec(
			DefaultAccount defaultAccount
			);
}
