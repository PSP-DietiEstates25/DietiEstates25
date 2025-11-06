package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.request.UserRequest;
import com.dietiestates.resource_server.dto.response.UserResponse;
import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.factory.UserFactory;
import com.dietiestates.resource_server.finder.RoleFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.mapper.UserMapper;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.repository.UserRepository;
import com.dietiestates.resource_server.service.NotificationCategoryService;
import com.dietiestates.resource_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
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

    private final NotificationCategoryService notificationCategoryService;

	@Override
	public UserResponse register(UserRequest request) throws RoleNotFoundException {
		
		var userSpec = userMapper.toSpec(request);
		var user = userFactory.createUserFromSpec(userSpec.getEmail());

		userRepository.save(user);

        createUserNotificationCategory(user.getEmail(), NotificationCategoryType.VISIT);
        createUserNotificationCategory(user.getEmail(), NotificationCategoryType.OFFER);
        createUserNotificationCategory(user.getEmail(), NotificationCategoryType.PROMOTIONAL);
        createUserNotificationCategory(user.getEmail(), NotificationCategoryType.NEW_PROPERTIES);

        return userMapper.fromEntity(user);
	}

    @Override
    public UserResponse getUserById(Long userid) {
        var user = userFinder.getUserById(userid);
        return userMapper.fromEntity(user);
    }

    public void createUserNotificationCategory(String userEmail, NotificationCategoryType categoryName) {

        var notificationCategoryRequest = NotificationCategoryRequest.builder()
                .name(categoryName.toString())
                .isActive(true)
                .userEmail(userEmail)
                .build();

        notificationCategoryService.createNotificationCategory(notificationCategoryRequest);
    }

}
