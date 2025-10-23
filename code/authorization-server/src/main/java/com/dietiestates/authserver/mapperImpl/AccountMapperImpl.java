package com.dietiestates.authserver.mapperImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.authserver.dto.request.AccountRequest;
import com.dietiestates.authserver.dto.response.AccountResponse;
import com.dietiestates.authserver.mapper.AccountMapper;
import com.dietiestates.authserver.model.Account;
import com.dietiestates.authserver.spec.AccountSpec;

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
