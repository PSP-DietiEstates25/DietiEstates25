package com.dietiestates.api.model;

import java.time.Instant;

import com.dietiestates.api.enums.VisitStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.ForeignKey;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "visit", indexes = {
		@Index(name = "IDX_visit_agent_start", columnList = "agent_id,start_at"),
		@Index(name = "IDX_visit_requester", columnList = "requester_id")
})
public class Visit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// annuncio di riferimento
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ad_id", foreignKey = @ForeignKey(name = "VISIT_AD_ID_FK"))
	private RealEstateAd ad;

	// chi che propone la visita
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_id", foreignKey = @ForeignKey(name = "VISIT_REQUESTER_ID_FK"))
	private User requester;

	// agente proprietario dell'annuncio
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", foreignKey = @ForeignKey(name = "VISIT_AGENT_ID_FK"))
	private User agent;

	// inizio/fine in utc
	@NotNull
	@Column(name = "start_at", nullable = false)
	private Instant startAt;

	@NotNull
	@Enumerated(EnumType.STRING)
	private VisitStatus status;

	@NotNull
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@PrePersist
	void onCreate() {
		Instant now = Instant.now();
		if (createdAt == null)
			createdAt = now;
		if (status == null)
			status = VisitStatus.PENDING;
	}
}
