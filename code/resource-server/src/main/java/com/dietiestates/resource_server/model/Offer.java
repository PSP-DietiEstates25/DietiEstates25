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
            name = "counter_of_offer_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "OFFER_OF_OFFER_ID")
    )
    private Offer counterOf = null;

    @OneToOne(mappedBy="counterOf", cascade = CascadeType.ALL, orphanRemoval = true)
    private Offer counterOffer = null;

	@Builder(builderMethodName = "offerBuilder")
	public Offer(
		String category,
		String status,
		Negotiation negotiation,
		BigDecimal amount
    ) {
		super(category, status, negotiation);
		this.amount = amount;
	}

    public void setCounterOfOffer(Offer counterOf){
        this.counterOf = counterOf;
        counterOf.setCounterOffer(this);
    }
}