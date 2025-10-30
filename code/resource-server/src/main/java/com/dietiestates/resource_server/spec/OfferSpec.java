package com.dietiestates.resource_server.spec;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferSpec extends ProposalSpec {

	private BigDecimal amount;
	private Long counteredOfferId;

	@Builder(builderMethodName = "offerSpecBuilder")
	public OfferSpec(
			String category,
			String status,
			String userEmail,
			BigDecimal amount,
            Long counteredOfferId
			) {
		super(category, status, userEmail);
		this.amount = amount;
        this.counteredOfferId = counteredOfferId;
	}
}
