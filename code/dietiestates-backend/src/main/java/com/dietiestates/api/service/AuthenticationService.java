package com.dietiestates.api.service;

import java.util.List;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;
import com.dietiestates.api.finder.RoleFinder;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	protected final RoleFinder roleFinder;
	
	private final UserRepository userRepository;
	
	protected final PasswordEncoder passwordEncoder;
	protected final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	public void register(AuthenticationRequest request) throws RoleNotFoundException {
		var userRole = roleFinder.getByRoleName("USER");
		var user = User
				.builder()
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.accountLocked(false)
				.enabled(true)
				.roles(List.of(userRole))
				.build()
				;
		userRepository.save(user);
	}
	
	public AuthenticationResponse login(AuthenticationRequest request) {
		
		var auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					request.getEmail(),
					request.getPassword()
				)
		);
		
		var user = ((User)auth.getPrincipal());
		var token = jwtService.generateToken(user);
		
		return AuthenticationResponse
				.builder()
				.token(token)
				.build()
				;
	}
}
