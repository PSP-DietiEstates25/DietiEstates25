package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.spec.AuthenticationSpec;

public interface UserMapper {

	AuthenticationSpec toSpec(AuthenticationRequest request);
}
