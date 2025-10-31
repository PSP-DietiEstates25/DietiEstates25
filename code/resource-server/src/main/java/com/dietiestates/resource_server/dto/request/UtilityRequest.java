package com.dietiestates.resource_server.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UtilityRequest {

    @NotNull(message = "Air conditioning is mandatory")
    private Boolean hasAirConditioning;

    @NotNull(message = "Doorman is mandatory")
    private Boolean hasDoorman;

    @NotNull(message = "Elevator is mandatory")
    private Boolean hasElevator;

    @NotNull(message = "Near school is mandatory")
    private Boolean nearSchool;

    @NotNull(message = "Near public transport is mandatory")
    private Boolean nearPublicTransport;

    @NotNull(message = "Near park is mandatory")
    private Boolean nearPark;
}
