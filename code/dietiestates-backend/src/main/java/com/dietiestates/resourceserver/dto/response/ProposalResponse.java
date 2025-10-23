package com.dietiestates.resourceserver.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProposalResponse {

	private Long id;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	private String category;
	private String status;
	private String userEmail;
	private Long realEstateId;
}
