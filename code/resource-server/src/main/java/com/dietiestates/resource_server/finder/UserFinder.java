package com.dietiestates.resource_server.finder;

import java.util.List;

import com.dietiestates.resource_server.exception.notfound.UserNotFoundException;
import com.dietiestates.resource_server.model.User;

public interface UserFinder {
		
	User getUserByEmail(String userEmail)
			throws UserNotFoundException;

    User getUserById(Long id)
        throws UserNotFoundException;
	
	List<User> getAllUsers();
}
