package com.dietiestates.resource_server.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
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

	@OneToOne(mappedBy = "geographicalPosition", cascade = CascadeType.ALL, orphanRemoval = true)
	private Detail detail;

	@Builder(builderMethodName = "builder")
	public GeographicalPosition(
			String city,
			String municipality,
			String address,
			Double latitude,
			Double longitude,
			Integer radius
			){
		this.city = city;
		this.municipality = municipality;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
	}
	
}
