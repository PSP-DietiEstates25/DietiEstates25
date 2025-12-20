package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.UserRequest;
import com.dietiestates.resource_server.dto.response.UserResponse;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.factory.UserFactory;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.mapper.UserMapper;
import com.dietiestates.resource_server.repository.UserRepository;
import com.dietiestates.resource_server.service.UserService;
import com.dietiestates.resource_server.verifier.UserVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceDefaultImpl implements UserService {

    private final UserFactory userFactory;
	private final UserFinder userFinder;
	private final UserMapper userMapper;
    private final UserVerifier userVerifier;
    private final UserRepository userRepository;

    @Override
    public UserResponse setupRegister(UserRequest request) {
        if(!userVerifier.checkUserAlreadyExists(request.getEmail()))
            return register(request);
        else
            return getUserByEmail(request.getEmail());
    }

    @Override
	public UserResponse register(UserRequest request) throws RoleNotFoundException {
		
		var userSpec = userMapper.toSpec(request);
		var user = userFactory.createUserFromSpec(userSpec.getEmail());

		userRepository.save(user);
        return userMapper.fromEntity(user);
	}

    @Override
    public UserResponse getUserById(Long userid) {
        var user = userFinder.getUserById(userid);
        return userMapper.fromEntity(user);
    }

    @Override
    public UserResponse getUserByEmail(String userEmail){
        var user = userFinder.getUserByEmail(userEmail);
        return userMapper.fromEntity(user);
    }
}
