package com.authorizationserver.api.mapperImpl;

import org.springframework.stereotype.Component;

import com.authorizationserver.api.dto.request.AccountRequest;
import com.authorizationserver.api.dto.response.AccountResponse;
import com.authorizationserver.api.mapper.AccountMapper;
import com.authorizationserver.api.model.Account;
import com.authorizationserver.api.spec.AccountSpec;

@Component
public class AccountMapperImpl implements AccountMapper {

	@Override
	public AccountSpec toSpec(AccountRequest request) {
		return AccountSpec.builder()
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
				.role(account.getAccountRole().getName().toString())
				.enabled(true)
				.locked(false)
				.build();
	}
}
