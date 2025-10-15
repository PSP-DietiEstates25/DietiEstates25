package com.authorizationserver.api.controller;

import javax.management.relation.RoleNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.authorizationserver.api.dto.request.AccountRequest;
import com.authorizationserver.api.dto.response.AccountResponse;
import com.authorizationserver.api.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class RegisterController {

	private final AuthenticationService authenticationService;
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<AccountResponse> register(
			@RequestBody @Valid AccountRequest request
			) throws RoleNotFoundException{
		var account = authenticationService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(account);
	}
}
