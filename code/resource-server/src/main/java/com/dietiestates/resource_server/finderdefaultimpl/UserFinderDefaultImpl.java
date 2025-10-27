package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.UserNotFoundException;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFinderDefaultImpl implements UserFinder {

	private final UserRepository userRepository;

	@Override
	public User getUserByEmail(String userEmail)
			throws UserNotFoundException {
		
		return userRepository.findByEmail(userEmail)
				.orElseThrow(UserNotFoundException::new);
	}

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
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
