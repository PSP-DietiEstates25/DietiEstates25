package com.dietiestates.resource_server.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
@Builder
public class RealEstateResponse {

    private Long id;
    private String category;
    private String[] images;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String estateAgentEmail;
    private Long detailId;
    private Long cadastralDataId;
}
