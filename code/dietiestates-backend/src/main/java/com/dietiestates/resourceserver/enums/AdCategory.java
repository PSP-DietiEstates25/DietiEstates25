package com.dietiestates.resourceserver.enums;

import java.util.Optional;

import lombok.Getter;

public enum AdCategory {
	SALE(0),
	RENT(1);

	@Getter
	private final int order;
	
	AdCategory(int order){
		this.order = order;
	}
	
	public static Optional<AdCategory> fromOrder(Integer orderCode) {
        if (orderCode == null) return Optional.empty();
        for (AdCategory adCategory: values()) {
            if (adCategory.getOrder() == orderCode) return Optional.of(adCategory);
        }
        throw new IllegalArgumentException("Invalid ad category code: " + orderCode);
    }
}
