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
    private Long counterOfId;
    private Long counterOfferId;
    private Boolean isRealEstateDeleted;

    @Builder(builderMethodName = "offerResponseBuilder")
    public OfferResponse(
            Long id,
            LocalDateTime createdDate,
            LocalDateTime lastModifiedDate,
            String category,
            String status,
            String userEmail,
            Long realEstateId,
            String estateAgentEmail,
            BigDecimal amount,
            Long counterOfId,
            Long counterOfferId,
            Boolean isRealEstateDeleted
    ) {
        super(
                id,
                createdDate,
                lastModifiedDate,
                category,
                status,
                userEmail,
                realEstateId,
                estateAgentEmail
        );
        this.amount = amount;
        this.counterOfId = counterOfId;
        this.counterOfferId = counterOfferId;
        this.isRealEstateDeleted = isRealEstateDeleted;
    }
}
