package com.dietiestates.api.serviceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;
import com.dietiestates.api.factory.AuthenticationFactory;
import com.dietiestates.api.finder.RoleFinder;
import com.dietiestates.api.mapper.UserMapper;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;
import com.dietiestates.api.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	protected final RoleFinder roleFinder;
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	private final AuthenticationFactory authenticationFactory;
	
	protected final PasswordEncoder passwordEncoder;
	protected final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	@Override
	public void register(AuthenticationRequest request) throws RoleNotFoundException {
		var authenticationSpec = userMapper.toSpec(request);
		var userRole = roleFinder.getByRoleName("USER");
		var user = authenticationFactory.createAccountFromSpec(authenticationSpec, passwordEncoder, userRole);
		userRepository.save(user);
	}
	
	@Override
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
