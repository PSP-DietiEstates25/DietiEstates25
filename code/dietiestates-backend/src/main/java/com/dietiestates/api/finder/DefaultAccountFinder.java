package com.dietiestates.api.finder;

import com.dietiestates.api.model.DefaultAccount;

public interface DefaultAccountFinder {

	DefaultAccount getDefaultAccountByEmail(String email);
}
