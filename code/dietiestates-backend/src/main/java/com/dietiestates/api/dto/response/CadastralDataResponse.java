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
public class CadastralDataResponse {

	private Long id;
	private Integer price;
	private Integer squareMeters;
	private String energyClass;
	private Integer rooms;
	private Integer floor;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	private Integer realEstateId;
}
