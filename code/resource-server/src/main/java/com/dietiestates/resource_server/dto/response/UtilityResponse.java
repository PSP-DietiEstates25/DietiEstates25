package com.dietiestates.resource_server.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UtilityResponse {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Boolean hasAirConditioning;
    private Boolean hasDoorman;
    private Boolean hasElevator;
    private Boolean nearSchool;
    private Boolean nearPublicTransport;
    private Boolean nearPark;
    private Long detailId;
}
