package com.dietiestates.authserver.service;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.core.Authentication;

import com.dietiestates.authserver.dto.request.AccountRequest;
import com.dietiestates.authserver.dto.response.AccountResponse;

public interface AuthenticationService {

	AccountResponse register(AccountRequest request) throws RoleNotFoundException;

	String login(Authentication authentication);
}
