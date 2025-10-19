package com.dietiestates.authserver.serviceImpl;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.authserver.dto.request.AccountRequest;
import com.dietiestates.authserver.dto.response.AccountResponse;
import com.dietiestates.authserver.factory.DefaultAccountFactory;
import com.dietiestates.authserver.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.authserver.finder.RoleFinder;
import com.dietiestates.authserver.mapper.AccountMapper;
import com.dietiestates.authserver.repository.DefaultAccountRepository;
import com.dietiestates.authserver.service.AuthenticationService;

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
