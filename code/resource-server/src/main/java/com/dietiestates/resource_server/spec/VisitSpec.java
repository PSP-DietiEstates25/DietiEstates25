package com.dietiestates.resource_server.spec;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitSpec extends ProposalSpec {

	private String date;
	private String time;
	
	@Builder(builderMethodName = "visitSpecBuilder")
	public VisitSpec(
			String category,
			String status,
			String userEmail,
			String date,
			String time
			) {
		super(category, status, userEmail);
		this.date = date;
		this.time = time;
	}
}
