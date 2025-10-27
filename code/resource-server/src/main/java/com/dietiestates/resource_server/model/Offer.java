package com.dietiestates.resource_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=true)
@Entity
@DiscriminatorValue("offer")
@EntityListeners(AuditingEntityListener.class)
public class Offer extends Proposal {

	@Column(nullable = true, precision = 14, scale = 2)
	private BigDecimal amount;
	
	@Builder(builderMethodName = "builder")
	public Offer(
		String category, 
		String status,
		User user,
		RealEstate realEstate,
		BigDecimal amount
			) {
		super(category, status, user, realEstate);
		this.amount = amount;
	}
}