package com.dietiestates.api.factory;

import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.User;

public interface UserFactory {

	User createUserFromSpec(
			DefaultAccount securityAccountDecorator
			);

}
