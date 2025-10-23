package com.dietiestates.resourceserver.finder;

import java.util.List;

import com.dietiestates.resourceserver.exception.notfound.UserNotFoundException;
import com.dietiestates.resourceserver.model.User;

public interface UserFinder {
		
	User getUserByEmail(String userEmail)
			throws UserNotFoundException;
	
	List<User> getAllUsers();
}
