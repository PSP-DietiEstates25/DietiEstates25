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
public class DetailResponse {

	private Long id;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	private Long geographicalPositionId;
	private Long utilityId;
	private Long searchId;
	private Long realEstateId;
}
