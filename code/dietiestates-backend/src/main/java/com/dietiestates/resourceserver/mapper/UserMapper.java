package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.AuthenticationRequest;
import com.dietiestates.resourceserver.spec.AuthenticationSpec;

public interface UserMapper {

	AuthenticationSpec toSpec(AuthenticationRequest request);
}
