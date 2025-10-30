package com.dietiestates.resource_server.model;

import jakarta.persistence.*;
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

    @OneToOne
    @JoinColumn(
            name = "countered_offer_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "COUNTERED_OFFER_OFFER_ID")
    )
    private Offer counteredOffer;

	@Builder(builderMethodName = "builder")
	public Offer(
		String category, 
		String status,
		User user,
		RealEstate realEstate,
		BigDecimal amount,
        Offer counteredOffer
			) {
		super(category, status, user, realEstate);
		this.amount = amount;
        this.counteredOffer = counteredOffer;
	}
}