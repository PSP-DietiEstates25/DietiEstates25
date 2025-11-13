package com.dietiestates.resource_server.model;

import com.dietiestates.resource_server.enums.EnergyClass;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
