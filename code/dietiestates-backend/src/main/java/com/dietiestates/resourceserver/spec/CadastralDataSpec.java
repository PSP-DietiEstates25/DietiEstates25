package com.dietiestates.resourceserver.spec;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CadastralDataSpec {

	private BigDecimal price;
	private Integer squareMeters;
	private String energyClass;
	private Integer rooms;
	private Integer floor;
}
