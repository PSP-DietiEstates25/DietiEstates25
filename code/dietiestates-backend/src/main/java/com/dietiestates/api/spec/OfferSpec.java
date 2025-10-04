package com.dietiestates.api.spec;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferSpec extends ProposalSpec {

	private BigDecimal amount;
	
	@Builder(builderMethodName = "offerSpecBuilder")
	public OfferSpec(
			String category,
			String status,
			String userEmail,
			BigDecimal amount
			) {
		super(category, status, userEmail);
		this.amount = amount;
	}
}
