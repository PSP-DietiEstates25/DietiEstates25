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
            foreignKey = @ForeignKey(name = "OFFER_COUNTER_OF_OFFER_ID")
    )
    private Offer counterOf;

    @OneToOne(mappedBy="counterOf", cascade = CascadeType.ALL, orphanRemoval = true)
    private Offer counterOffer;

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

    public void setConterOf(Offer counteredOffer){
        this.counterOf = counteredOffer;
        counteredOffer.setCounterOffer(this);
    }
}