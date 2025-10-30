package com.dietiestates.auth.service;

import com.dietiestates.auth.dto.request.ChangePasswordRequest;
import com.dietiestates.auth.finder.DefaultAccountFinder;
import com.dietiestates.auth.model.DefaultAccount;
import com.dietiestates.auth.repository.DefaultAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final DefaultAccountFinder defaultAccountFinder;
    private final PasswordEncoder passwordEncoder;
    private final DefaultAccountRepository defaultAccountRepository;

    @Transactional
    public void changeOwnPassword(Principal principal, ChangePasswordRequest request) {

        DefaultAccount defaultAccount = defaultAccountFinder.getDefaultAccountByEmail(principal.getName());

        checkPasswordMatched(request.getOldPassword(), defaultAccount.getAccountPassword());

        defaultAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        defaultAccountRepository.save(defaultAccount);
        // tokenRepository.revokeAllFor(acc.getId());
    }

    private void checkPasswordMatched(String rawPassword, String actualPassword){

        if (!passwordEncoder.matches(rawPassword, actualPassword))
            throw new BadCredentialsException("Old password and actual password doesn't match.");

    }
}
