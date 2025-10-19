package com.dietiestates.resourceserver.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StafferSpec extends AuthenticationSpec {

	private String adminEmail;
	
	@Builder(builderMethodName = "stafferSpecBuilder")
	public StafferSpec(
			String email,
			String adminEmail
			) {
		super(email);
		this.adminEmail = adminEmail;
	}
}
