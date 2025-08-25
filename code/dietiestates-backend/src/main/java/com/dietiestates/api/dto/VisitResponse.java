package com.dietiestates.api.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VisitResponse {
    private Long id;
    private Long adId;
    private String adAddress;
    private String requesterEmail;
    private String agentEmail;
    private String status;   // enum name
    private Instant startAt; // UTC
    private Instant createdAt;
}
