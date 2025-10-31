package com.dietiestates.resource_server.dto.request;

import java.math.BigDecimal;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfferRequest extends ProposalRequest {

    @NotEmpty(message = "Amount is mandatory")
    @NotBlank(message = "Amount is mandatory")
    @Positive(message = "Amount must be a positive number")
    private BigDecimal amount;

    @Nullable
    private Long counteredOfferId;

    @Builder(builderMethodName = "offerDtoBuilder")
    public OfferRequest(
            String category,
            String status,
            String userEmail,
            BigDecimal amount
    ) {
        super(category, status, userEmail);
        this.amount = amount;
    }
}
