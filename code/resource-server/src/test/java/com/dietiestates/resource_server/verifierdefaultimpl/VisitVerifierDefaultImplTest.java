package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.finder.VisitFinder;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.repository.VisitRepository;
import com.dietiestates.resource_server.verifierdefaultimpl.VisitVerifierDefaultImpl;
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
class VisitVerifierDefaultImplTest {

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private VisitFinder visitFinder;

    @Mock
    private RealEstateFinder realEstateFinder;

    @InjectMocks
    private VisitVerifierDefaultImpl visitVerifier;

    @Mock
    private Visit visit;

    @Mock
    private Negotiation negotiation;

    @Mock
    private RealEstate realEstateFromNegotiation;

    @Mock
    private RealEstate realEstateFromFinder;

    @Test
    @DisplayName("checkVisitOwnedByRealEstate: visita dell'immobile indicato → nessuna eccezione")
    void checkVisitOwnedByRealEstate_whenSameRealEstate_doesNotThrow() {
        Long visitId = 20L;
        Long realEstateId = 5L;

        when(visitFinder.getVisitById(visitId)).thenReturn(visit);
        when(visit.getNegotiation()).thenReturn(negotiation);
        when(negotiation.getRealEstate()).thenReturn(realEstateFromNegotiation);
        when(realEstateFromNegotiation.getId()).thenReturn(realEstateId);

        when(realEstateFinder.getRealEstateById(realEstateId)).thenReturn(realEstateFromFinder);
        when(realEstateFromFinder.getId()).thenReturn(realEstateId);

        assertDoesNotThrow(() -> visitVerifier.checkVisitOwnedByRealEstate(visitId, realEstateId));
    }

    @Test
    @DisplayName("checkVisitOwnedByRealEstate: visita appartiene ad un altro immobile → eccezione")
    void checkVisitOwnedByRealEstate_whenDifferentRealEstate_throwsException() {
        Long visitId = 20L;
        Long realEstateId = 5L;

        when(visitFinder.getVisitById(visitId)).thenReturn(visit);
        when(visit.getNegotiation()).thenReturn(negotiation);
        when(negotiation.getRealEstate()).thenReturn(realEstateFromNegotiation);
        when(realEstateFromNegotiation.getId()).thenReturn(99L); // diverso

        when(realEstateFinder.getRealEstateById(realEstateId)).thenReturn(realEstateFromFinder);
        when(realEstateFromFinder.getId()).thenReturn(realEstateId);

        assertThrows(VisitNotOwnedByRealEstateException.class,
                () -> visitVerifier.checkVisitOwnedByRealEstate(visitId, realEstateId));
    }
}
