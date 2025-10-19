package com.dietiestates.resourceserver.dto.response;

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
			String email,
			String adminEmail,
			List<RealEstateResponse> createdRealEstates
			) {
		super(id, email, adminEmail);
		this.createdRealEstates = createdRealEstates;
	}
}
