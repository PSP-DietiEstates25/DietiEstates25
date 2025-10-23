package com.dietiestates.resourceserver.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.resourceserver.enums.EnergyClass;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CadastralData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer squareMeters;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnergyClass energyClass;

    @Column(nullable = false)
    private Integer rooms;

    @Column(nullable = false)
    private Integer floor;
    
    @CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToOne(mappedBy = "cadastralData", cascade = CascadeType.ALL, orphanRemoval = true)
	private RealEstate realEstate;
	
	@Builder(builderMethodName = "builder")
	public CadastralData(
		BigDecimal price,
		Integer squareMeters,
		String energyClass,
		Integer rooms,
		Integer floor
		) {
		this.price = price;
		this.squareMeters = squareMeters;
		this.energyClass = EnergyClass.valueOf(energyClass);
		this.rooms = rooms;
		this.floor = floor;
	}

}
