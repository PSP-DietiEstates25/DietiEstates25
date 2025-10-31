package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AccountResponse;
import com.dietiestates.api.mapper.AccountMapper;
import com.dietiestates.api.model.Account;
import com.dietiestates.api.spec.AuthenticationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountMapperImpl implements AccountMapper {

	@Override
	public AuthenticationSpec toSpec(AuthenticationRequest request) {
		return AuthenticationSpec.builder()
				.email(request.getEmail())
				.password(request.getPassword())
				.enabled(true)
				.locked(false)
				.build();
	}

	@Override
	public AccountResponse fromEntity(Account account) {
		return AccountResponse.builder()
				.id(account.getAccountId())
				.email(account.getAccountEmail())
				.role(account.getAccountRole())
				.enabled(true)
				.locked(false)
				.build();
	}
	
}
