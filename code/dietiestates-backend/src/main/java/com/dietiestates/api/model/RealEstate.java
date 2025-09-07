package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;

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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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
    private byte[] photo;

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
    @JoinColumn(nullable = false, name = "estate_agent_id", foreignKey = @ForeignKey(name = "ESTATE_AGENT_ID_FK"))
    private User estateAgent;

    @ManyToOne
    @JoinColumn(nullable = false, name = "detail_id", foreignKey = @ForeignKey(name = "DETAIL_ID_FK"))
    private Detail detail;

    public void addEstateAgent(User estateAgent) {
        this.estateAgent = estateAgent;
        // TODO: settare anche il contrario se nel model User esiste la collection es.
        // user.getAds().add(this);
    }

    public void addDetail(Detail d) {
        this.setDetail(d);
        d.getAds().add(this);
    }
}
