package com.dietiestates.auth.factory;

import org.springframework.stereotype.Component;

import com.dietiestates.auth.model.DefaultAccount;
import com.dietiestates.auth.model.SecurityAccount;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityAccountFactory {

    public SecurityAccount createSecurityAccountDecoratorFromSpec(
            DefaultAccount defaultAccount
    ) {
        return SecurityAccount.builder()
                .defaultAccount(defaultAccount)
                .enabled(true)
                .locked(false)
                .build();
    }
}
