package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.UserRequest;
import com.dietiestates.resource_server.dto.response.UserResponse;

public interface UserService {
    UserResponse register(UserRequest request);
    UserResponse getUserById(Long userid);
}
