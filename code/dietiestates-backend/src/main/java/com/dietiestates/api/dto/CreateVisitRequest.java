package com.dietiestates.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateVisitRequest(
        @NotNull Long adId,

        // data/ora locali proposte
        @NotNull @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date, // "2028-05-02"
        @NotNull @Min(0) @Max(23) Integer hour,                           // 0..23
        @NotNull @Min(0) @Max(59) Integer minute,                         // 0..59

        @Pattern(regexp = "^[A-Za-z_]+(?:/[A-Za-z_]+)*$") String timezone) {
}
