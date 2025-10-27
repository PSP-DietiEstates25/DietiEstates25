package com.dietiestates.auth.finder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.dietiestates.auth.model.DefaultAccount;
import com.dietiestates.auth.repository.DefaultAccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultAccountFinder {

    private final DefaultAccountRepository defaultAccountRepository;

    public DefaultAccount getDefaultAccountByEmail(String email) {
        return defaultAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account with specified email not found"));
    }
}
