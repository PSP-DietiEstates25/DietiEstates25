package com.dietiestates.resource_server.spec;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

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
