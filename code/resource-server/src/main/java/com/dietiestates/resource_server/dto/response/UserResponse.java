package com.dietiestates.resource_server.dto.response;

import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.model.Proposal;
import com.dietiestates.resource_server.model.Search;
import lombok.*;

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
    private List<Notification> notifications = new ArrayList<>();
    private List<Search> searches = new ArrayList<>();
    private List<Proposal> proposals = new ArrayList<>();

}
