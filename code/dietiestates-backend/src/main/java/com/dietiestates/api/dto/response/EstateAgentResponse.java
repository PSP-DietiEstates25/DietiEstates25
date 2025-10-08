package com.dietiestates.api.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EstateAgentResponse extends StafferResponse {

	private List<RealEstateResponse> createdRealEstates;
	
	@Builder(builderMethodName = "estateAgentResponseBuilder")
	public EstateAgentResponse(
			Long id,
			AccountResponse account,
			String adminEmail,
			List<RealEstateResponse> createdRealEstates
			) {
		super(id, account, adminEmail);
		this.createdRealEstates = createdRealEstates;
	}
}
