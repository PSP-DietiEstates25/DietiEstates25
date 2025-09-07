package com.dietiestates.api.model;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.ProposalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;
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
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Offer extends Proposal {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "real_estate_id", nullable = false, foreignKey = @ForeignKey(name = "OFFER_REAL_ESTATE_ID_FK"))
	private RealEstate realEstate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "OFFER_USER_ID_FK"))
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estate_agent_id", nullable = false, foreignKey = @ForeignKey(name = "OFFER_ESTATE_AGENT_ID_FK"))
	private User estateAgent;

	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ProposalStatus status;
}