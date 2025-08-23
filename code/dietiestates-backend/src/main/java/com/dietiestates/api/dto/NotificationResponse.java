package com.dietiestates.api.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private String category;
    private String title;
    private String message;
    private Long adId;
    private Boolean read;
    private Instant createdAt;
}
