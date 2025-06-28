<<<<<<< HEAD
package com.dietiestates25.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "real_estates")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal price;

    @NotNull
    private double size;

    @NotNull
    private int rooms;

    @NotNull
    private int floor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EnergyClass energyClass;

    @NotNull
    @OneToOne(mappedBy = "realEstate")
    private Ad ad;

    @NotNull
    @OneToOne(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = false)
    private GeographicalPosition geographicalPosition;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "services_id")
    private Service services;
=======
package com.dietiestates25.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import com.dietiestates25.backend.model.Service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "real_estates")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal price;

    @NotNull
    private double size;

    @NotNull
    private int rooms;

    @NotNull
    private int floor;

    @Enumerated(EnumType.STRING)
    private EnergyClass energyClass;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id", unique = true)
    private Ad ad;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "services_id")
    private Service services;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geographical_position_id")
    private GeographicalPosition geographicalPosition;


    // Metodi:

    // Verifica se l'immobile è attivo
    public boolean isActive() {
        return ad != null && ad.getDeletedAt() == null;
    }

    /* 
    // Aggiunge un servizio all'immobile
    public void addService(Service service) {
        if (service != null) {
            if (this.services == null) {
                this.services = new Service();
            }
            this.services.addService(service);
            service.setRealEstate(this);
        }
    }
    */

    // Ritorna l'annuncio associato all'immobile
    public Ad getAd() {
        return ad;
    }   
>>>>>>> 814a2508f3ddb55a2f8ecdf03740c745c05a0a08
}