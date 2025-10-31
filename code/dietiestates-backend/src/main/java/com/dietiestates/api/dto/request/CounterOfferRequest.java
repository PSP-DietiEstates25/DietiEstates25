package com.dietiestates.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CounterOfferRequest(
        @NotNull @Min(1) BigDecimal amount,
        String message) {
}
