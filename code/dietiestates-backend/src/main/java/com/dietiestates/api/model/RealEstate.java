package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.dto.RealEstateDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdCategory category;

    @Lob
    @Column(nullable = false)
    private String[] images;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Float size;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer rooms;

    @Column(nullable = false)
    private Integer floor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnergyClass energyClass;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(
    		nullable = false,
    		name = "estate_agent_id",
    		foreignKey = @ForeignKey(name = "REAL_ESTATE_ESTATE_AGENT_ID_FK"))
    private User estateAgent;

    @ManyToOne
    @JoinColumn(
    		nullable = false,
    		name = "detail_id",
    		foreignKey = @ForeignKey(name = "REAL_ESTATE_DETAIL_ID_FK"))
    private Detail detail;
    
    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Proposal> proposals = new ArrayList<>();
    
    public void addProposal(Proposal proposal) {
    	proposals.add(proposal);
    	proposal.setRealEstate(this); 
    }
    
    public static RealEstate of(RealEstateDto request) {
    	//category images description price size address rooms floor energyClass latitude longitude
    	return RealEstate.builder()
    			.category(AdCategory.valueOf(request.getCategory()))
    			.images(request.getImages())
    			.description(request.getDescription())
    			.price(request.getPrice())
    			.size(request.getSize())
    			.address(request.getAddress())
    			.rooms(request.getRooms())
    			.floor(request.getFloor())
    			.energyClass(EnergyClass.valueOf(request.getEnergyClass()))
    			.latitude(request.getLatitude())
    			.longitude(request.getLongitude())
    			.build();
    }
}
