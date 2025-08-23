package com.dietiestates.api.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateNotificationPreferencesRequest(
        @NotEmpty List<@Valid Item> items) {
    public record Item(
            @NotNull String category,
            @NotNull Boolean enabled) {
    }
}
