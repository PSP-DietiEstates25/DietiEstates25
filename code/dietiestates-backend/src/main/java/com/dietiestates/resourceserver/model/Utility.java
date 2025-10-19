package com.dietiestates.resourceserver.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Utility {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	@OneToOne(mappedBy = "utility", cascade = CascadeType.ALL, orphanRemoval = true)
	private Detail detail;
	
	@Builder(builderMethodName = "builder")
	public Utility(
			Boolean hasElevator,
			Boolean hasDoorman,
			Boolean hasAirConditioning
			) {
		this.hasElevator = hasElevator;
		this.hasDoorman = hasDoorman;
		this.hasAirConditioning = hasAirConditioning;
	}

}
