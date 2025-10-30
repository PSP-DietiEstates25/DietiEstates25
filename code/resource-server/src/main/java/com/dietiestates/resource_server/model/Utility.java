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

    @Column(nullable = false)
    private Boolean nearSchool;

    @Column(nullable = false)
    private Boolean nearPublicTransport;

    @Column(nullable = false)
    private Boolean nearPark;
	
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
			Boolean hasAirConditioning,
            Boolean nearSchool,
            Boolean nearPublicTransport,
            Boolean nearPark
			) {
		this.hasElevator = hasElevator;
		this.hasDoorman = hasDoorman;
		this.hasAirConditioning = hasAirConditioning;
        this.nearSchool = nearSchool;
        this.nearPublicTransport = nearPublicTransport;
        this.nearPark = nearPark;
	}

}
