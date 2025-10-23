package com.dietiestates.resourceserver.spec;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
