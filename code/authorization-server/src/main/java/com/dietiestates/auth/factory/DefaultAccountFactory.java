package com.dietiestates.auth.factory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.auth.model.DefaultAccount;
import com.dietiestates.auth.model.Role;
import com.dietiestates.auth.spec.AuthSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultAccountFactory {

    public DefaultAccount createAccountFromSpec(
            AuthSpec spec,
            PasswordEncoder passwordEncoder,
            Role role
    ) {
        return DefaultAccount.builder()
                .email(spec.getEmail())
                .password(passwordEncoder.encode(spec.getPassword()))
                .role(role)
                .build();
    }
}
