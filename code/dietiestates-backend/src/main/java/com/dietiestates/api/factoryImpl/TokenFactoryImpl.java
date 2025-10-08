package com.dietiestates.api.factoryImpl;

import com.dietiestates.api.factory.TokenFactory;
import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.Token;

public class TokenFactoryImpl implements TokenFactory {

	public Token createTokenFromSpec(
			String token,
			DefaultAccount defaultAccount
			) {
		return Token.builder()
				.defaultAccount(defaultAccount)
				.token(token)
				.build();
	}
}
