package com.dietiestates.api.dto;

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
public class VisitDto extends ProposalDto {

	@NotEmpty(message = "Date is mandatory")
	@NotBlank(message = "Date is mandatory")
	@Future(message = "Date must be in the future")
	private LocalDate date;
	
	@NotEmpty(message = "Time is mandatory")
	@NotBlank(message = "Time is mandatory")
	@Future(message = "Time must be in the future")
	private LocalTime time;
	
	@Builder(builderMethodName = "visitDtoBuilder")
	public VisitDto(
			String category,
			String status,
			String userEmail,
			LocalDate date,
			LocalTime time
			) {
		super(category, status, userEmail);
		this.date = date;
		this.time = time;
	}
}
