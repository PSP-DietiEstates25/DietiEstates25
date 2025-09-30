package com.dietiestates.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class GeographicalPosition {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private String municipality;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private Double latitude;
	
	@Column(nullable = false)
	private Double longitude;
	
	@Column(nullable = true)
	private Integer radius;
	
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
			foreignKey = @ForeignKey(name = "GEOGRAPHICAL_POSITION_DETAIL_ID_FK"))
	private Detail detail;

	@Builder(builderMethodName = "geographicalPositionBuilder")
	public GeographicalPosition(
			String city,
			String municipality,
			String address,
			Double latitude,
			Double longitude,
			Integer radius,
			LocalDateTime createdDate,
			Detail detail
			){
		this.city = city;
		this.municipality = municipality;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.createdDate = createdDate;
		this.setDetail(detail);
	}
	
	public void setDetail(Detail detail) {
		this.detail = detail;
		detail.setGeographicalPosition(this);
	}
}
