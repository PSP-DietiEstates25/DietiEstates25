package com.dietiestates.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

@Data
public class OfferProposalRequest {
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be > 0")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount; // wrapper
}
