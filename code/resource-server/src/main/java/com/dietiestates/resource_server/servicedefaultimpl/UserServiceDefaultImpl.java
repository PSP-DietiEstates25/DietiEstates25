package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.UserRequest;
import com.dietiestates.resource_server.dto.response.UserResponse;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.factory.UserFactory;
import com.dietiestates.resource_server.finder.RoleFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.mapper.UserMapper;
import com.dietiestates.resource_server.repository.UserRepository;
import com.dietiestates.resource_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("authenticationServiceImpl")
@Primary
@RequiredArgsConstructor
public class UserServiceDefaultImpl implements UserService {

    private final UserFactory userFactory;
	private final UserFinder userFinder;
	private final UserMapper userMapper;
    private final UserRepository userRepository;

    protected final RoleFinder roleFinder;

	@Override
	public UserResponse register(UserRequest request) throws RoleNotFoundException {
		
		var userSpec = userMapper.toSpec(request);
		var userRole = roleFinder.getByRoleName("ROLE_USER");
		
		var user = userFactory.createUserFromSpec(userSpec.getEmail());

		userRepository.save(user);

        return userMapper.fromEntity(user);
	}

    @Override
    public UserResponse getUserById(Long userid) {
        var user = userFinder.getUserById(userid);
        return userMapper.fromEntity(user);
    }
}
