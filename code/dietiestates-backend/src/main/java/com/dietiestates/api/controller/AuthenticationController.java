package com.dietiestates.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.api.dto.AuthenticationResponse;
import com.dietiestates.api.dto.LoginRequest;
import com.dietiestates.api.dto.RegistrationRequest;
import com.dietiestates.api.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<?> register(
			@RequestBody @Valid RegistrationRequest request
	){
		authenticationService.register(request);
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(
			@RequestBody @Valid LoginRequest request
	){
		return ResponseEntity.ok(authenticationService.login(request));
	}
}
