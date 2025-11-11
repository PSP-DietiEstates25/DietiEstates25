package com.dietiestates.resource_server.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VisitResponse extends ProposalResponse {

    private LocalDate date;
    private LocalTime time;

    @Builder(builderMethodName = "visitResponseBuilder")
    public VisitResponse(
            Long id,
            LocalDateTime createdDate,
            LocalDateTime lastModifiedDate,
            String category,
            String status,
            String userEmail,
            Long realEstateId,
            String estateAgentEmail,
            LocalDate date,
            LocalTime time
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
        this.date = date;
        this.time = time;
    }
}
