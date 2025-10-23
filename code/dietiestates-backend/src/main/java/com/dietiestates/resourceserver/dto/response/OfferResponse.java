package com.dietiestates.resourceserver.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfferResponse extends ProposalResponse {

	private BigDecimal amount;
	
	@Builder(builderMethodName = "offerResponseBuilder")
	public OfferResponse(
		Long id,
		LocalDateTime createdDate,
		LocalDateTime lastModifiedDate,
		String category,
		String status,
		String userEmail,
		Long realEstateId,
		BigDecimal amount
			) {
		super(
				id,
				createdDate,
				lastModifiedDate,
				category,
				status,
				userEmail,
				realEstateId
				);
		this.amount = amount;
	}
}
