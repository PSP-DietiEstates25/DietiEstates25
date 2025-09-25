package com.dietiestates.api.enums;

import lombok.Getter;

public enum ProposalCategory {
	OFFER(0),
	VISIT(1);
	
	@Getter
	private final int order;
	
	ProposalCategory(int order){
		this.order = order;
	}
}