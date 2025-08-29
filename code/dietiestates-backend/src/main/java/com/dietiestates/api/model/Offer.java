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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;
import jakarta.validation.constraints.NotNull;
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
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "offer", indexes = {
		@Index(name = "IDX_offer_agent_created", columnList = "estate_agent_id,created_date"),
		@Index(name = "IDX_offer_realestate", columnList = "real_estate_id")
})
@EntityListeners(AuditingEntityListener.class)
public class Offer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "real_estate_id", nullable = false, foreignKey = @ForeignKey(name = "OFFER_REAL_ESTATE_ID_FK"))
	private RealEstate realEstate;

	// chi propone (deve essere CLIENT)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "OFFER_USER_ID_FK"))
	private User user;

	// destinatario (l’agente proprietario dell’annuncio)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estate_agent_id", nullable = false, foreignKey = @ForeignKey(name = "OFFER_ESTATE_AGENT_ID_FK"))
	private User estateAgent;

	@NotNull
	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal amount;

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ProposalStatus status;
}