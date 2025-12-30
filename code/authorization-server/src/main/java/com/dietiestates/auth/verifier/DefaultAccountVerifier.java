package com.dietiestates.auth.verifier;

import com.dietiestates.auth.exception.alreadyexists.DefaultAccountAlreadyExistsException;
import com.dietiestates.auth.exception.notfound.DefaultAccountNotFoundException;
import com.dietiestates.auth.repository.DefaultAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAccountVerifier {

    private final DefaultAccountRepository defaultAccountRepository;

    private Boolean checkDefaultAccountExists(String email){
        return defaultAccountRepository.existsByEmail(email);
    }

    public void checkDefaultAccountAlreadyExists(String email){
        if(!checkDefaultAccountExists(email))
            throw new DefaultAccountNotFoundException();
    }

    public void checkDefaultAccountDoesntExists(String email){
        if(checkDefaultAccountExists(email))
            throw new DefaultAccountAlreadyExistsException();
    }
}
