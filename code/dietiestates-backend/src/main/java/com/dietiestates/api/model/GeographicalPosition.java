package com.dietiestates.api.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
@EntityListeners(AuditingEntityListener.class)
public class GeographicalPosition {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private String municipality;
	
	@Column(nullable = false)
	private Double zoneMarkerLatitude;
	
	@Column(nullable = false)
	private Double zoneMarkerLongitude;
	
	@Column(nullable = false)
	private Float zoneMarkerRadius;
	
	@NotNull
	@OneToOne(mappedBy = "geographicalPosition", cascade = CascadeType.ALL, orphanRemoval = true)
	private Detail detail;
	
	public void addDetail(Detail detail) {
		this.setDetail(detail);
		detail.setGeographicalPosition(this);
	}
}
