package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.AuthenticationRequest;
import com.dietiestates.resourceserver.dto.response.AuthenticationResponse;
import com.dietiestates.resourceserver.exception.notfound.RoleNotFoundException;
import com.dietiestates.resourceserver.factory.AccountFactory;
import com.dietiestates.resourceserver.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.resourceserver.factory.UserFactory;
import com.dietiestates.resourceserver.finder.RoleFinder;
import com.dietiestates.resourceserver.mapper.UserMapper;
import com.dietiestates.resourceserver.model.SecurityAccountDecorator;
import com.dietiestates.resourceserver.repository.DefaultAccountRepository;
import com.dietiestates.resourceserver.repository.UserRepository;
import com.dietiestates.resourceserver.service.AuthenticationService;

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
		var userRole = roleFinder.getByRoleName("ROLE_USER");
		
		var defaultAccount = defaultAccountFactory.createAccountFromSpec(authenticationSpec, passwordEncoder, userRole);
		var securityAccountDecorator = secutiryAccountDecoratorFactory.createSecurityAccountDecoratorFromSpec(defaultAccount);
		
		var user = userFactory.createUserFromSpec(defaultAccount);
		defaultAccountRepository.save(defaultAccount);
		userRepository.save(user);
	}
	
	@Override
	public AuthenticationResponse login(Authentication authentication) {
		
		var user = ((SecurityAccountDecorator)authentication.getPrincipal());
		var token = jwtService.generateToken(user);
		
		return AuthenticationResponse
				.builder()
				.token(token)
				.build()
				;
	}
}
