package com.dietiestates.authorization.mapperImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.authorization.dto.request.RegisterRequest;
import com.dietiestates.authorization.dto.response.AccountResponse;
import com.dietiestates.authorization.mapper.AccountMapper;
import com.dietiestates.authorization.model.SecurityAccountDecorator;
import com.dietiestates.authorization.spec.AccountSpec;

@Component
public class AccountMapperImpl implements AccountMapper {

	@Override
	public AccountSpec toSpec(RegisterRequest request) {
		return AccountSpec.builder()
				.email(request.getEmail())
				.password(request.getPassword())
				.enabled(true)
				.locked(false)
				.build();
	}

	@Override
	public AccountResponse fromEntity(SecurityAccountDecorator account) {
		return AccountResponse.builder()
				.id(account.getDefaultAccount().getId())
				.email(account.getDefaultAccount().getEmail())
				.role(account.getDefaultAccount().getRole().getName().toString())
				.enabled(true)
				.locked(false)
				.build();
	}
}
