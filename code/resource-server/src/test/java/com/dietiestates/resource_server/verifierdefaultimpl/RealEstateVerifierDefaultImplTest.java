package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.RealEstateNotOwnedByEstateAgentException;
import com.dietiestates.resource_server.repository.RealEstateRepository;
import com.dietiestates.resource_server.verifierdefaultimpl.RealEstateVerifierDefaultImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RealEstateVerifierDefaultImplTest {

    @Mock
    private RealEstateRepository realEstateRepository;

    @InjectMocks
    private RealEstateVerifierDefaultImpl realEstateVerifier;

    @Test
    @DisplayName("checkRealEstateOwnedByEstateAgent: immobile di quell'agente → nessuna eccezione")
    void checkRealEstateOwnedByEstateAgent_whenOwned_doesNotThrow() {
        Long realEstateId = 1L;
        String estateAgentEmail = "agent@example.com";

        when(realEstateRepository.existsByIdAndEstateAgentEmail(realEstateId, estateAgentEmail))
                .thenReturn(true);

        assertDoesNotThrow(() -> realEstateVerifier.checkRealEstateOwnedByEstateAgent(realEstateId, estateAgentEmail));
    }

    @Test
    @DisplayName("checkRealEstateOwnedByEstateAgent: immobile non di quell'agente → eccezione")
    void checkRealEstateOwnedByEstateAgent_whenNotOwned_throwsException() {
        Long realEstateId = 1L;
        String estateAgentEmail = "other@example.com";

        when(realEstateRepository.existsByIdAndEstateAgentEmail(realEstateId, estateAgentEmail))
                .thenReturn(false);

        assertThrows(RealEstateNotOwnedByEstateAgentException.class,
                () -> realEstateVerifier.checkRealEstateOwnedByEstateAgent(realEstateId, estateAgentEmail));
    }
}