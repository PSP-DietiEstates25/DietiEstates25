package com.authorizationserver.api.serviceImpl;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authorizationserver.api.dto.request.AccountRequest;
import com.authorizationserver.api.dto.response.AccountResponse;
import com.authorizationserver.api.factory.DefaultAccountFactory;
import com.authorizationserver.api.factory.SecurityAccountDecoratorFactory;
import com.authorizationserver.api.finder.RoleFinder;
import com.authorizationserver.api.mapper.AccountMapper;
import com.authorizationserver.api.repository.DefaultAccountRepository;
import com.authorizationserver.api.service.AuthenticationService;

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
	//private final AuthenticationManager authenticationManager;
	
	@Override
	public AccountResponse register(AccountRequest request) throws RoleNotFoundException {
		
		var authenticationSpec = accountMapper.toSpec(request);
		
		var accountRole = roleFinder.getByRoleName("ROLE_USER");
		var defaultAccount = defaultAccountFactory.createAccountFromSpec(authenticationSpec, passwordEncoder, accountRole);
		var securityAccountDecorator = securityAccountDecoratorFactory.createSecurityAccountDecoratorFromSpec(defaultAccount);
		
		defaultAccountRepository.save(defaultAccount);
		return accountMapper.fromEntity(securityAccountDecorator);
	}

	@Override
	public String login(Authentication authentication) {
		return "";
	}
}
