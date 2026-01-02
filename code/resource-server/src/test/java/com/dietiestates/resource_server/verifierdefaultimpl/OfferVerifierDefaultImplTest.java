package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;
import com.dietiestates.resource_server.finder.OfferFinder;
import com.dietiestates.resource_server.finder.RealEstateFinder;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.repository.OfferRepository;
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
class OfferVerifierDefaultImplTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferFinder offerFinder;

    @Mock
    private RealEstateFinder realEstateFinder;

    @InjectMocks
    private OfferVerifierDefaultImpl offerVerifier;

    @Mock
    private Offer offer;

    @Mock
    private Negotiation negotiation;

    @Mock
    private RealEstate realEstateFromNegotiation;

    @Mock
    private RealEstate realEstateFromFinder;

    @Test
    @DisplayName("checkOfferOwnedByRealEstate: offerta appartiene allo stesso immobile → nessuna eccezione")
    void checkOfferOwnedByRealEstate_whenSameRealEstate_doesNotThrow() {
        Long offerId = 10L;
        Long realEstateId = 5L;

        when(offerFinder.getOfferById(offerId)).thenReturn(offer);
        when(offer.getNegotiation()).thenReturn(negotiation);
        when(negotiation.getRealEstate()).thenReturn(realEstateFromNegotiation);
        when(realEstateFromNegotiation.getId()).thenReturn(realEstateId);

        when(realEstateFinder.getRealEstateById(realEstateId)).thenReturn(realEstateFromFinder);
        when(realEstateFromFinder.getId()).thenReturn(realEstateId);

        assertDoesNotThrow(() -> offerVerifier.checkOfferOwnedByRealEstate(offerId, realEstateId));
    }

    @Test
    @DisplayName("checkOfferOwnedByRealEstate: offerta di un altro immobile → eccezione")
    void checkOfferOwnedByRealEstate_whenDifferentRealEstate_throwsException() {
        Long offerId = 10L;
        Long realEstateId = 5L;

        when(offerFinder.getOfferById(offerId)).thenReturn(offer);
        when(offer.getNegotiation()).thenReturn(negotiation);
        when(negotiation.getRealEstate()).thenReturn(realEstateFromNegotiation);
        // ID dell'immobile associato all'offerta diverso da quello passato al metodo
        when(realEstateFromNegotiation.getId()).thenReturn(99L);

        when(realEstateFinder.getRealEstateById(realEstateId)).thenReturn(realEstateFromFinder);
        when(realEstateFromFinder.getId()).thenReturn(realEstateId);

        assertThrows(OfferNotOwnedByRealEstateException.class,
                () -> offerVerifier.checkOfferOwnedByRealEstate(offerId, realEstateId));
    }
}
