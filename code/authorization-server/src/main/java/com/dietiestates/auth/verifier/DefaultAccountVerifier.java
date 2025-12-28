package com.dietiestates.auth.verifier;

import com.dietiestates.auth.exception.notfound.AccountNotFoundException;
import com.dietiestates.auth.repository.DefaultAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAccountVerifier {

    private final DefaultAccountRepository defaultAccountRepository;

    public Boolean checkDefaultAccountAlreadyExists(String email){
        if(!defaultAccountRepository.existsByEmail(email))
            throw new AccountNotFoundException();

        return true;
    }
}
