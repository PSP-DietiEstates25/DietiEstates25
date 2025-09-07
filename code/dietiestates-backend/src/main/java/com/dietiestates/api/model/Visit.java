package com.dietiestates.api.model;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.VisitStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Visit extends Proposal {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "real_estate_id", foreignKey = @ForeignKey(name = "VISIT_REAL_ESTATE_ID_FK"))
	private RealEstate realEstate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "VISIT_USER_ID_FK"))
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "estate_agent_id", foreignKey = @ForeignKey(name = "VISIT_ESTATE_AGENT_ID_FK"))
	private User estateAgent;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VisitStatus status;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
}
