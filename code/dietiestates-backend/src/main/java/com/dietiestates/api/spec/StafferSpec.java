package com.dietiestates.api.spec;

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
	
	@Builder(builderMethodName = "StafferSpecBuilder")
	public StafferSpec(
			String email,
			String password,
			String role,
			String adminEmail
			) {
		super(email, password, role);
		this.adminEmail = adminEmail;
	}
}
