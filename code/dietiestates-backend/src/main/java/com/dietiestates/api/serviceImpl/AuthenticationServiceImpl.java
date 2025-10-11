package com.dietiestates.api.serviceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;
import com.dietiestates.api.factory.AccountFactory;
import com.dietiestates.api.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.api.factory.UserFactory;
import com.dietiestates.api.finder.RoleFinder;
import com.dietiestates.api.mapper.UserMapper;
import com.dietiestates.api.model.SecurityAccountDecorator;
import com.dietiestates.api.repository.DefaultAccountRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service("authenticationServiceImpl")
@Primary
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	protected final AccountFactory defaultAccountFactory;
	protected final SecurityAccountDecoratorFactory secutiryAccountDecoratorFactory;
	protected final DefaultAccountRepository defaultAccountRepository;
	
	protected final RoleFinder roleFinder;
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	private final UserFactory userFactory;
	
	protected final PasswordEncoder passwordEncoder;
	protected final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	@Override
	public void register(AuthenticationRequest request) throws RoleNotFoundException {
		
		var authenticationSpec = userMapper.toSpec(request);
		var userRole = roleFinder.getByRoleName("USER");
		
		var defaultAccount = defaultAccountFactory.createAccountFromSpec(authenticationSpec, passwordEncoder, userRole);
		var securityAccountDecorator = secutiryAccountDecoratorFactory.createSecurityAccountDecoratorFromSpec(defaultAccount);
		
		var user = userFactory.createUserFromSpec(defaultAccount);
		defaultAccountRepository.save(defaultAccount);
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
		
		var user = ((SecurityAccountDecorator)auth.getPrincipal());
		var token = jwtService.generateToken(user);
		
		return AuthenticationResponse
				.builder()
				.token(token)
				.build()
				;
	}
}
