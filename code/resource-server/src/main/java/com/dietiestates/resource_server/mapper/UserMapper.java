package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.UserRequest;
import com.dietiestates.resource_server.dto.response.UserResponse;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.UserSpec;

public interface UserMapper {
	UserSpec toSpec(UserRequest request);
    UserResponse fromEntity(User user);
}
