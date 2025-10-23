package com.dietiestates.resourceserver.enums;

import java.util.Optional;

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
	
	public static Optional<ProposalStatus> fromOrder(Integer orderCode) {
        if (orderCode == null) return Optional.empty();
        for (ProposalStatus proposalStatus: values()) {
            if (proposalStatus.getOrder() == orderCode) return Optional.of(proposalStatus);
        }
        throw new IllegalArgumentException("Invalid proposal status code: " + orderCode);
    }
}
