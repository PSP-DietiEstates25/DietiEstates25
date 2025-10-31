package com.dietiestates.resource_server.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class SearchResponse {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer size;
    private Integer page;
    private String userEmail;
    private Long cadastralFilterId;
    private Long detailId;
}
