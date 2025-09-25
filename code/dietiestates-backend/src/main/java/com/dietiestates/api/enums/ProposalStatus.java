package com.dietiestates.api.enums;

import lombok.Getter;

public enum ProposalStatus {
	PENDING(0),
	ACCEPTED(1),
	REJECTED(2);
	
	@Getter
	private final int order;
	
	ProposalStatus(int order){
		this.order = order;
	}
}
