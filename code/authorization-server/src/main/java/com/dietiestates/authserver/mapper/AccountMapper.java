package com.dietiestates.authserver.mapper;

import com.dietiestates.authserver.dto.request.AccountRequest;
import com.dietiestates.authserver.dto.response.AccountResponse;
import com.dietiestates.authserver.model.Account;
import com.dietiestates.authserver.spec.AccountSpec;

public interface AccountMapper {

	AccountSpec toSpec(AccountRequest request);
	
	AccountResponse fromEntity(Account account);
}