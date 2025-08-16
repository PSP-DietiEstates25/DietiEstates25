package com.dietiestates.api.model;

import java.math.BigDecimal;

import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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
public class RealEstateAd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DATI ANNUNCIO
    @EqualsAndHashCode.Exclude
    @NotNull
    private AdCategory category; // SALE | RENT

    @EqualsAndHashCode.Exclude
    @NotNull
    @Lob
    private byte[] photo;

    @EqualsAndHashCode.Exclude
    @NotNull
    private String description;

    // DATI IMMOBILE
    @EqualsAndHashCode.Exclude
    @NotNull
    private BigDecimal price;

    @EqualsAndHashCode.Exclude
    @NotNull
    private Float size;

    @EqualsAndHashCode.Exclude
    @NotNull
    private String address;

    @EqualsAndHashCode.Exclude
    @NotNull
    private Integer rooms;

    @EqualsAndHashCode.Exclude
    @NotNull
    private Integer floor;

    @EqualsAndHashCode.Exclude
    @NotNull
    private EnergyClass energyClass;

    @EqualsAndHashCode.Exclude
    @NotNull
    private Double latitude;

    @EqualsAndHashCode.Exclude
    @NotNull
    private Double longitude;

    // RELAZIONI
    @EqualsAndHashCode.Exclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "estate_agent_email", foreignKey = @ForeignKey(name = "ESTATE_AGENT_EMAIL_FK"))
    private EstateAgent estateAgent;

    @EqualsAndHashCode.Exclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "detail_id", foreignKey = @ForeignKey(name = "DETAIL_ID_FK"))
    private Detail detail;

    public void attachEstateAgent(EstateAgent agent) {
        this.setEstateAgent(agent);
        agent.getPublishedAd().add(this);
    }

    public void attachDetail(Detail d) {
        this.setDetail(d);
        d.getAds().add(this);
    }
}