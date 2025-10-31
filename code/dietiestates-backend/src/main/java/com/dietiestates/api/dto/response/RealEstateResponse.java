package com.dietiestates.api.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
	private String[] proximityTags;
}
