package com.dietiestates.resource_server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ProposalRequest {

    @NotEmpty(message = "Category is mandatory")
    @NotBlank(message = "Category is mandatory")
    private String category;

    @NotEmpty(message = "Status is mandatory")
    @NotBlank(message = "Status is mandatory")
    private String status;
}