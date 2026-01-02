package com.dietiestates.resource_server.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
