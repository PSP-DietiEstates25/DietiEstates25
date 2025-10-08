package com.dietiestates.api.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Future;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VisitRequest extends ProposalRequest {

	@NotEmpty(message = "Date is mandatory")
	@NotBlank(message = "Date is mandatory")
	private String date;
	
	@NotEmpty(message = "Time is mandatory")
	@NotBlank(message = "Time is mandatory")
	private String time;
	
	@Builder(builderMethodName = "visitDtoBuilder")
	public VisitRequest(
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
