package com.dietiestates.resourceserver.service;

import org.springframework.security.core.Authentication;

import com.dietiestates.resourceserver.dto.request.AuthenticationRequest;
import com.dietiestates.resourceserver.dto.response.AuthenticationResponse;
import com.dietiestates.resourceserver.exception.notfound.RoleNotFoundException;

public interface AuthenticationService {

	void register(AuthenticationRequest request) throws RoleNotFoundException;

	AuthenticationResponse login(Authentication authentication);
}
