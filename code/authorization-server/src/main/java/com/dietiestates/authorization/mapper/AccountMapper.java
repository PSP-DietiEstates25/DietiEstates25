package com.dietiestates.authorization.mapper;

import com.dietiestates.authorization.dto.request.RegisterRequest;
import com.dietiestates.authorization.dto.response.AccountResponse;
import com.dietiestates.authorization.model.SecurityAccountDecorator;
import com.dietiestates.authorization.spec.AccountSpec;

public interface AccountMapper {

	AccountSpec toSpec(RegisterRequest request);
	
	AccountResponse fromEntity(SecurityAccountDecorator account);
}