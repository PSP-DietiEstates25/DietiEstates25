package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.model.User;

public interface UserFinder {
		
	User getUserByEmail(String userEmail)
			throws UserNotFoundException;
}
