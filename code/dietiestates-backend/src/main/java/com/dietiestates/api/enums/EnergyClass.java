package com.dietiestates.api.enums;

import lombok.Getter;

public enum EnergyClass {
	A4(0),
	A3(1),
	A2(2),
	A1(3),
	B(4),
	C(5),
	D(6),
	E(7),
	F(8),
	G(9);
	
	@Getter
	private final int order;
	
	EnergyClass(int order){
		this.order = order;
	}
}
