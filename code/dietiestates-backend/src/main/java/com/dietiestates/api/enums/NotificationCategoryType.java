package com.dietiestates.api.enums;

import lombok.Getter;

public enum NotificationCategoryType {
	NEW_PROPERTIES(0),
	PROMOTIONAL(1),
	VISIT(2),
	OFFER(3);
	
	@Getter
	private final int order;
	
	NotificationCategoryType(int order){
		this.order = order;
	}
}
