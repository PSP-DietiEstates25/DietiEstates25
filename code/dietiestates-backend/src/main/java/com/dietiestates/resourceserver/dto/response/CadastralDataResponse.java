package com.dietiestates.resourceserver.dto.response;

import java.math.BigDecimal;
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
	private BigDecimal price;
	private Integer squareMeters;
	private String energyClass;
	private Integer rooms;
	private Integer floor;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	private Long realEstateId;
}
