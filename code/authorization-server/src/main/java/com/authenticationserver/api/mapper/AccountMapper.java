package com.authenticationserver.api.mapper;

import com.authenticationserver.api.dto.request.AccountRequest;
import com.authenticationserver.api.dto.response.AccountResponse;
import com.authenticationserver.api.model.Account;
import com.authenticationserver.api.spec.AccountSpec;

public interface AccountMapper {

	AccountSpec toSpec(AccountRequest request);
	
	AccountResponse fromEntity(Account account);
}