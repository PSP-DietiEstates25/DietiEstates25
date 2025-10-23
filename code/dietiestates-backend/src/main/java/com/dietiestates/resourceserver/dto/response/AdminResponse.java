package com.dietiestates.resourceserver.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminResponse extends StafferResponse {

	private List<StafferResponse> createdStaffers;
	
	@Builder(builderMethodName = "adminResponseBuilder")
	public AdminResponse(
			Long id,
			String email,
			String adminEmail,
			List<StafferResponse> createdStaffers
			) {
		super(id, email, adminEmail);
		this.createdStaffers = createdStaffers;
	}
}
