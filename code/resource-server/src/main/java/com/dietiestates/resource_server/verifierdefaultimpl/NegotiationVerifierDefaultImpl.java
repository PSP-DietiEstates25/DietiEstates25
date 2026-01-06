package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.repository.NegotiationRepository;
import com.dietiestates.resource_server.verifier.NegotiationVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NegotiationVerifierDefaultImpl implements NegotiationVerifier {

    private final NegotiationRepository negotiationRepository;

    @Override
    public boolean checkNegotiationAlreadyExists(String userEmail, Long realEstateId, String estateAgentEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return negotiationRepository.existsActiveExternalNegotiation(realEstateId, estateAgentEmail);
        }
        return negotiationRepository.existsByUserEmailAndRealEstateId(userEmail, realEstateId);
    }
}
