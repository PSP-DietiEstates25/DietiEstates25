package com.authenticationserver.api.factory;

import com.authenticationserver.api.model.DefaultAccount;
import com.authenticationserver.api.model.SecurityAccountDecorator;

public interface SecurityAccountDecoratorFactory {

	SecurityAccountDecorator createSecurityAccountDecoratorFromSpec(
			DefaultAccount defaultAccount
			);
}
