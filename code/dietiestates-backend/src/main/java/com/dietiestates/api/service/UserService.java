package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	public User getUserByEmail(String userEmail) {
		return userRepository.findByEmail(userEmail)
				.orElseThrow(UserNotFoundException::new);
	}
}
