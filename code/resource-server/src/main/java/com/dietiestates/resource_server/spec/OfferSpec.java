package com.dietiestates.resource_server.spec;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferSpec extends ProposalSpec {

	private BigDecimal amount;
	private Long counterOfId;

	@Builder(builderMethodName = "offerSpecBuilder")
	public OfferSpec(
			String category,
			String status,
			BigDecimal amount,
            Long counterOfId
    ) {
		super(category, status);
		this.amount = amount;
        this.counterOfId = counterOfId;
	}
}
