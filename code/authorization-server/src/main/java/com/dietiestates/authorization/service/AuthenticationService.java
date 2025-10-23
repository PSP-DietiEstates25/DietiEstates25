package com.dietiestates.authorization.service;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.core.Authentication;

import com.dietiestates.authorization.dto.request.RegisterRequest;
import com.dietiestates.authorization.dto.response.AccountResponse;

public interface AuthenticationService {

	AccountResponse register(RegisterRequest request) throws RoleNotFoundException;
}
