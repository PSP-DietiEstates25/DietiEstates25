package com.authorizationserver.api.mapper;

import com.authorizationserver.api.dto.request.AccountRequest;
import com.authorizationserver.api.dto.response.AccountResponse;
import com.authorizationserver.api.model.Account;
import com.authorizationserver.api.spec.AccountSpec;

public interface AccountMapper {

	AccountSpec toSpec(AccountRequest request);
	
	AccountResponse fromEntity(Account account);
}