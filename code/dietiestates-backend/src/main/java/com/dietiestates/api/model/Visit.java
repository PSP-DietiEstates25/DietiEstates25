package com.dietiestates.api.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@DiscriminatorValue("visit")
@EntityListeners(AuditingEntityListener.class)
public class Visit extends Proposal {

	@Column(nullable = false)
	private LocalDate date;
	
	@Column(nullable = false)
	private LocalTime time;
	
	@Builder(builderMethodName = "visitBuilder")
	public Visit(
			Long id, 
			ProposalCategory category, 
			ProposalStatus status, 
			LocalDateTime createdAt,
			LocalDateTime lastModifiedDate,
			EstateAgent estateAgent,
			User user,
			RealEstate realEstate,
			LocalDate date,
			LocalTime time
	) {
		super(id, category, status, createdAt, lastModifiedDate, estateAgent, user, realEstate);
		this.date = date;
		this.time = time;
	}
}
