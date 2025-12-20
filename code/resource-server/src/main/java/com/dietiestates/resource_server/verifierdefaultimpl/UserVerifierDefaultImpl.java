package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.repository.UserRepository;
import com.dietiestates.resource_server.verifier.UserVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserVerifierDefaultImpl implements UserVerifier {

    private final UserRepository userRepository;

    @Override
    public Boolean checkUserAlreadyExists(String userEmail) {
        return userRepository.existsByEmail(userEmail);
    }
}
