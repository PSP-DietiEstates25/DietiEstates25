package com.dietiestates.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationPreferenceResponse {
    private String category;
    private Boolean enabled;
}
