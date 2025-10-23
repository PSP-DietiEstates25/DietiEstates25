package com.dietiestates.authorization.serviceImpl;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.authorization.dto.request.RegisterRequest;
import com.dietiestates.authorization.dto.response.AccountResponse;
import com.dietiestates.authorization.factory.DefaultAccountFactory;
import com.dietiestates.authorization.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.authorization.finder.RoleFinder;
import com.dietiestates.authorization.mapper.AccountMapper;
import com.dietiestates.authorization.repository.DefaultAccountRepository;
import com.dietiestates.authorization.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final DefaultAccountFactory defaultAccountFactory;
	private final SecurityAccountDecoratorFactory securityAccountDecoratorFactory;
	private final DefaultAccountRepository defaultAccountRepository;
	
	private final RoleFinder roleFinder;
	private final AccountMapper accountMapper;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public AccountResponse register(RegisterRequest request) throws RoleNotFoundException {
		
		var authenticationSpec = accountMapper.toSpec(request);
		
		var accountRole = roleFinder.getByRoleName("USER");
		var defaultAccount = defaultAccountFactory.createAccountFromSpec(authenticationSpec, passwordEncoder, accountRole);
		var securityAccountDecorator = securityAccountDecoratorFactory.createSecurityAccountDecoratorFromSpec(defaultAccount);
		
		defaultAccountRepository.save(defaultAccount);
		return accountMapper.fromEntity(securityAccountDecorator);
	}
}
