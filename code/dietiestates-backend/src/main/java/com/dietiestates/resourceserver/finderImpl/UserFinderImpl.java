package com.dietiestates.resourceserver.finderImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.UserNotFoundException;
import com.dietiestates.resourceserver.finder.UserFinder;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFinderImpl implements UserFinder {

	private final UserRepository userRepository;

	@Override
	public User getUserByEmail(String userEmail)
			throws UserNotFoundException {
		
		return userRepository.findByEmail(userEmail)
				.orElseThrow(UserNotFoundException::new);
	}
	
	@Override
	public List<User> getAllUsers() {
		
		var usersIterable = userRepository.findAll();
		var allUsers = new ArrayList<User>();
		usersIterable.forEach(allUsers::add);
		
		return allUsers;
	}
}
