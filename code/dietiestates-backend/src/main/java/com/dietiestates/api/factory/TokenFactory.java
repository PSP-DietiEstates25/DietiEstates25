package com.dietiestates.api.factory;

import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.Token;

public interface TokenFactory {

	Token createTokenFromSpec(
			String token,
			DefaultAccount defaultAccount
			);
}
