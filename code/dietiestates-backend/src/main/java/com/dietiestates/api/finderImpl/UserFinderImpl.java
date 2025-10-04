package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFinderImpl implements UserFinder {

	private final UserRepository userRepository;

	@Override
	public User getUserByEmail(String userEmail) throws UserNotFoundException {
		return userRepository.findByEmail(userEmail)
				.orElseThrow(UserNotFoundException::new);
	}
}
