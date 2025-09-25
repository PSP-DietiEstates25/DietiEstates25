package com.dietiestates.api.enums;

import lombok.Getter;

public enum AdCategory {
	SALE(0),
	RENT(1);

	@Getter
	private final int order;
	
	AdCategory(int order){
		this.order = order;
	}
}
