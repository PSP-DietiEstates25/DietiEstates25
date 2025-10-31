package com.dietiestates.resource_server.enums;

import java.util.Optional;

import lombok.Getter;

public enum ProposalCategory {
    OFFER(0),
    COUNTER_OFFER(1),
    VISIT(2);

    @Getter
    private final int order;

    ProposalCategory(int order){
        this.order = order;
    }

    public static Optional<ProposalCategory> fromOrder(Integer orderCode) {
        if (orderCode == null) return Optional.empty();
        for (ProposalCategory proposalCategory: values()) {
            if (proposalCategory.getOrder() == orderCode) return Optional.of(proposalCategory);
        }
        throw new IllegalArgumentException("Invalid proposal category code: " + orderCode);
    }
}
