package com.dietiestates25.backend.model;

import jakarta.persistence.*;

import io.micrometer.common.lang.NonNull;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private boolean hasElevator;

    @NonNull
    @Column
    private boolean hasDoorman;

    @NonNull
    @Column
    private boolean hasAirConditioning;

    @OneToOne(mappedBy = "services")
    private RealEstate realEstate;

    public Service() {
    }

    // GETTER / SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}