package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.UserRequest;
import com.dietiestates.resource_server.dto.response.UserResponse;

public interface UserService {
    UserResponse setupRegister(UserRequest request);
    UserResponse register(UserRequest request);
    UserResponse getUserById(Long userid);
    UserResponse getUserByEmail(String userEmail);
}
