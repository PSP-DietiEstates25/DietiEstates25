package com.dietiestates.resource_server.dto.response;

import com.dietiestates.resource_server.model.*;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
