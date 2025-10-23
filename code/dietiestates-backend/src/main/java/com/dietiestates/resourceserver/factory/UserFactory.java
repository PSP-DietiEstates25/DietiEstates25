package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.User;

public interface UserFactory {

	User createUserFromSpec(
			String email
			);
}
