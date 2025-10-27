package com.dietiestates.resource_server.dto.request;

import jakarta.validation.constraints.Positive;
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
public class DetailRequest {

    @Positive(message = "Geographical position id must be a positive number")
    private Long geographicalPositionId;

    @Positive(message = "Utility id must be a positive number")
    private Long utilityId;
}

