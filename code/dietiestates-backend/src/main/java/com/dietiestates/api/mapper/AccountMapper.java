package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AccountResponse;
import com.dietiestates.api.model.Account;
import com.dietiestates.api.spec.AuthenticationSpec;

public interface AccountMapper {

	AuthenticationSpec toSpec(AuthenticationRequest request);
	
	AccountResponse fromEntity(Account account);
}
