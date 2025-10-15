package com.authenticationserver.api.service;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.core.Authentication;

import com.authenticationserver.api.dto.request.AccountRequest;
import com.authenticationserver.api.dto.response.AccountResponse;

public interface AuthenticationService {

	AccountResponse register(AccountRequest request) throws RoleNotFoundException;

	String login(Authentication authentication);
}
