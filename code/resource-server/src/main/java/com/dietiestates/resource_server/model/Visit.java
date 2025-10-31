package com.dietiestates.resource_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper=true)
@Entity
@NoArgsConstructor
@DiscriminatorValue("visit")
@EntityListeners(AuditingEntityListener.class)
public class Visit extends Proposal {

	@Column(nullable = true)
	private LocalDate date;
	
	@Column(nullable = true)
	private LocalTime time;
	
	@Builder(builderMethodName = "builder")
	public Visit(
			String category, 
			String status, 
			User user,
			RealEstate realEstate,
			LocalDate date,
			LocalTime time
	) {
		super(category, status, user, realEstate);
		this.date = date;
		this.time = time;
	}
}
