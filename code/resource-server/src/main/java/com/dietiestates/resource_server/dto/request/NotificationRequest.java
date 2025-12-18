package com.dietiestates.resource_server.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class NotificationRequest {

    @NotEmpty(message = "Message is mandatory")
    @NotBlank(message = "Message is mandatory")
    private String message;

    @NotEmpty(message = "Notification category is mandatory")
    @NotBlank(message = "Notification category is mandatory")
    private String notificationCategory;

    @NotNull(message = "Visibility is mandatory")
    private Boolean isVisible;

    @Positive(message = "Negotiation id must be a positive number")
    private Long negotiationId;
}
