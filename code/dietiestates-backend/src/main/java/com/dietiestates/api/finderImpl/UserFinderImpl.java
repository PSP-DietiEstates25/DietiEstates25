package com.dietiestates.api.finderImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.finder.DefaultAccountFinder;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFinderImpl implements UserFinder {

	private final UserRepository userRepository;
	private final DefaultAccountFinder defaultAccountFinder;

	@Override
	public User getUserByEmail(String userEmail)
			throws UserNotFoundException {
		
		var account = defaultAccountFinder.getDefaultAccountByEmail(userEmail);
		var allUsers = getAllUsers();
		
		for(User user: allUsers) {
			if(user.getSecurityAccountDecorator().getAccountEmail().equals(account.getEmail())){
				return user;
			}
		}
		
		throw new UserNotFoundException();
	}
	
	@Override
	public List<User> getAllUsers() {
		
		var usersIterable = userRepository.findAll();
		var allUsers = new ArrayList<User>();
		usersIterable.forEach(allUsers::add);
		
		return allUsers;
	}
}
