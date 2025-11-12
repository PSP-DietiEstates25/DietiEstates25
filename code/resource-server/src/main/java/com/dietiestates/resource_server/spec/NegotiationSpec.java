package com.dietiestates.resource_server.spec;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NegotiationSpec {
    private String userEmail;
    private String estateAgentEmail;
    private Long realEstateId;
}
