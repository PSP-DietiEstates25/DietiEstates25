package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@DiscriminatorValue("offer")
@EntityListeners(AuditingEntityListener.class)
public class Offer extends Proposal {

	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal amount;
	
	@Builder(builderMethodName = "offerBuilder")
	public Offer(
			Long id, 
			ProposalCategory category, 
			ProposalStatus status,
			LocalDateTime createdAt,
			LocalDateTime lastModifiedDate,
			User user,
			RealEstate realEstate,
			BigDecimal amount
	) {
		super(id, category, status, createdAt, lastModifiedDate, user, realEstate);
		this.amount = amount;
	}
}