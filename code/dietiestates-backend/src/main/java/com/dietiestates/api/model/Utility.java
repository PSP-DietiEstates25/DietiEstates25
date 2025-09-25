package com.dietiestates.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Utility {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	private Boolean hasAirConditioning;
	
	@Column(nullable = false)
	private Boolean hasDoorman;

	@Column(nullable = false)
	private Boolean hasElevator;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;

	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "detail_id",
			foreignKey = @ForeignKey(name = "UTILITY_DETAIL_ID_FK"))
	private Detail detail;
	
	@Builder(builderMethodName = "utilityBuilder")
	public Utility(
			LocalDateTime createdDate,
			Boolean hasElevator,
			Boolean hasDoorman,
			Boolean hasAirConditioning,
			Detail detail
			) {
		this.createdDate = createdDate;
		this.hasElevator = hasElevator;
		this.hasDoorman = hasDoorman;
		this.hasAirConditioning = hasAirConditioning;
		this.setDetail(detail);
	}
	
	public void setDetail(Detail detail) {
		this.detail = detail;
		detail.setUtility(this);
	}
}
