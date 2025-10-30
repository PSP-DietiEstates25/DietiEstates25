package com.dietiestates.resource_server.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfferResponse extends ProposalResponse {

    private BigDecimal amount;
    private Long counteredOfferId;

    @Builder(builderMethodName = "offerResponseBuilder")
    public OfferResponse(
            Long id,
            LocalDateTime createdDate,
            LocalDateTime lastModifiedDate,
            String category,
            String status,
            String userEmail,
            Long realEstateId,
            BigDecimal amount,
            Long counteredOfferId
    ) {
        super(
                id,
                createdDate,
                lastModifiedDate,
                category,
                status,
                userEmail,
                realEstateId
        );
        this.amount = amount;
        this.counteredOfferId = counteredOfferId;
    }
}
