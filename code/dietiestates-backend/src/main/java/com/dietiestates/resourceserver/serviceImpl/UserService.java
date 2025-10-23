package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.exception.notfound.UserNotFoundException;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
}
