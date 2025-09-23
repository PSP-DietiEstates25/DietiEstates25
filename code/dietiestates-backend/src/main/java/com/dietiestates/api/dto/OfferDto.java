package com.dietiestates.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfferDto extends ProposalDto{

	@NotEmpty(message = "Amount is mandatory")
	@NotBlank(message = "Amount is mandatory")
	@Positive(message = "Amount must be a positive number")
	private BigDecimal amount;
	
	@Builder(builderMethodName = "offerDtoBuilder")
	public OfferDto(
			String category,
			String status,
			String userEmail,
			BigDecimal amount
			) {
		super(category, status, userEmail);
		this.amount = amount;
	}
}
