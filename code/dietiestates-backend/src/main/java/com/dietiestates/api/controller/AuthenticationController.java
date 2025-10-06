package com.dietiestates.api.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;
import com.dietiestates.api.model.User;
import com.dietiestates.api.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	@Qualifier("authenticationServiceImpl")
	private final AuthenticationService authenticationService;
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<User> register(
			@RequestBody @Valid AuthenticationRequest request
			) throws RoleNotFoundException{
		authenticationService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(
			@RequestBody @Valid AuthenticationRequest request
	){
		return ResponseEntity.ok(authenticationService.login(request));
	}
}