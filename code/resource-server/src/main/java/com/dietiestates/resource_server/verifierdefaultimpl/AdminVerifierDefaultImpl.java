package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.repository.AdminRepository;
import com.dietiestates.resource_server.verifier.AdminVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminVerifierDefaultImpl implements AdminVerifier {

}
