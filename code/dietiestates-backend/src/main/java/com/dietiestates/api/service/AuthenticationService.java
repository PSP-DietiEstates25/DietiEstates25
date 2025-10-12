package com.dietiestates.api.service;

import org.springframework.security.core.Authentication;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;

public interface AuthenticationService {

	void register(AuthenticationRequest request) throws RoleNotFoundException;

	AuthenticationResponse login(Authentication authentication);
}
